from django.contrib.auth import get_user_model
from django.utils.crypto import get_random_string

User = get_user_model()

def update_user_info(user_id, new_email, new_username):
    user = User.objects.get(id=user_id)

    if User.objects.filter(email=new_email).exists():
        base_email, domain = new_email.split("@")
        new_email = f"{base_email}_{get_random_string(4)}@{domain}"

    user.email = new_email

    if User.objects.filter(username=new_username).exists():
        new_username = f"{new_username}_{get_random_string(4)}"

    user.username = new_username
    user.save()
