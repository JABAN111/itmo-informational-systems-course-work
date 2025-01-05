<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Вход</title>
    <style>
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
            background-color: #f5f5f5;
        }

        .login-container {
            display: flex;
            width: 90%;
            max-width: 1200px;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            border-radius: 10px;
            overflow: hidden;
        }

        .login-form {
            flex: 1;
            background: #fff;
            padding: 40px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
        }

        .login-form h1 {
            font-size: 32px;
            color: #5e5ec2;
            margin-bottom: 20px;
        }

        .form-group {
            width: 100%;
            margin-bottom: 20px;
        }

        input {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            border: 2px solid #5e5ec2;
            border-radius: 5px;
            outline: none;
        }

        input:focus {
            border-color: #433eae;
        }

        .btn-submit {
            background: #433eae;
            color: #fff;
            font-size: 16px;
            padding: 12px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            width: 100%;
        }

        .btn-submit:hover {
            background: #2d29a6;
        }

        .register-link {
            margin-top: 20px;
            font-size: 14px;
            color: #777;
        }

        .register-link a {
            color: #433eae;
            text-decoration: none;
        }

        .login-image {
            flex: 1;
            position: relative;
            background: #eee;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-image img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .image-overlay {
            position: relative;
            text-align: center;
        }

        .notification {
            position: absolute;
            top: 10%;
            right: 10%;
            background: #f5f5f5;
            border: 1px solid #5e5ec2;
            border-radius: 10px;
            padding: 10px 20px;
            font-size: 14px;
            color: #433eae;
            box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.1);
            z-index: 1;
        }
    </style>
</head>
<body>
<div class="login-container">
    <div class="login-form">
        <h1>Вход</h1>
        <form action="${url.loginAction}" method="post">
            <div class="form-group">
                <input type="text" id="username" name="username" placeholder="Логин" required>
            </div>
            <div class="form-group">
                <input type="password" id="password" name="password" placeholder="Пароль" required>
            </div>
            <div class="form-group">
                <button type="submit" class="btn-submit">Продолжить</button>
            </div>
        </form>
<#--        <p class="register-link">-->
<#--            У вас нет аккаунта? <a href="${url.registerUrl}">Зарегистрируйтесь</a>.-->
<#--        </p>-->
    </div>
    <div class="login-image">
        <div class="image-overlay">
            <#--            <div class="notification">-->
            <#--                <span>🔒</span> Password Reset<br>Your password has been restored successfully-->
            <#--            </div>-->
            <img src="https://se.ifmo.ru/~s368601/course/login-image.png" alt="Ravenclaw Style">
        </div>
    </div>
</div>
</body>
</html>