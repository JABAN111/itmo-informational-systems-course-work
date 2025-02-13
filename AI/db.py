import time
from typing import Any, Dict
from datetime import datetime
import os

from sqlalchemy import create_engine, Column, Integer, String, TIMESTAMP, select, update
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker
from pydantic import BaseModel

DATABASE_URL = os.getenv("DATABASE_URL", "postgresql://ai:user@pg_ai:5432/course")
engine = create_engine(DATABASE_URL)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()

class QueueItemDB(Base):
    __tablename__ = "queue"

    id = Column(Integer, primary_key=True, index=True)
    artifact_name = Column(String, nullable=False)
    future_category = Column(String, nullable=False)
    status = Column(String, default="pending")
    created_at = Column(TIMESTAMP, default=datetime.now())

class QueueItem(BaseModel):
    artifact_name: str
    staff_name: str
    future_category: str

class QueueService:
    @staticmethod
    def add_or_update_queue(artifact_name: str, future_category: str) -> Dict[str, Any]:
        with SessionLocal() as session:
            existing_item = session.query(QueueItemDB).filter_by(artifact_name=artifact_name).first()

            if existing_item:
                existing_item.future_category = future_category
                existing_item.status = "updated"
                message = "Артефакт обновлен в очереди"
            else:
                new_item = QueueItemDB(
                    artifact_name=artifact_name,
                    future_category=future_category,
                    status="pending",
                    created_at=datetime.utcnow(),
                )
                session.add(new_item)
                message = "Артефакт добавлен в очередь"

            session.commit()
            return {"message": message, "artifact_name": artifact_name, "future_category": future_category}


    @staticmethod
    def get_next_from_queue() -> Dict[str, Any]:
        with SessionLocal() as session:
            stmt = select(QueueItemDB).where(QueueItemDB.status == "pending").order_by(QueueItemDB.created_at.asc())
            item = session.execute(stmt).scalars().first()
            if not item:
                return {"error": "Очередь пуста"}
            return {
                "id": item.id,
                "artifact_name": item.artifact_name,
                "staff_name": item.staff_name,
                "future_category": item.future_category,
                "status": item.status,
                "created_at": item.created_at,
            }

    @staticmethod
    def process_queue_item(item_id: int) -> Dict[str, Any]:
        with SessionLocal() as session:
            stmt = update(QueueItemDB).where(QueueItemDB.id == item_id).values(status="processed")
            result = session.execute(stmt)
            session.commit()
            if result.rowcount == 0:
                return {"error": "Элемент не найден"}
            return {"message": "Элемент обработан"}

def init_db():
    while True:
        try:
            engine = create_engine(DATABASE_URL)
            SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
            Base.metadata.create_all(engine)
            break
        except Exception as e:
            print(f" Ошибка подключения к БД: {e}")
            print(" Повторная попытка через 5 секунд...")
            time.sleep(5)


if(__name__ == "__main__"):
    init_db()
