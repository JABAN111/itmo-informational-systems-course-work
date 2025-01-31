from setuptools import setup, find_packages

def get_dependencies(req_file="requirements.txt"):
    dependencies = []
    with open(req_file, "r", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line:
                package, version = line.split("==")
                dependencies.append(f"{package}>={version}")
    return dependencies

name = "AI"
version = "1.0.1"
description = "Some description of the package"

dependencies = get_dependencies()

setup(
    name=name,
    version=version,
    description=description,
    packages=find_packages(),
    install_requires=dependencies,
)
