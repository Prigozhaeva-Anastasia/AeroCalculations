# Используйте базовый образ
FROM ubuntu:latest

# Обновите пакеты и установите утилиты locales
RUN apt-get update && \
    apt-get install -y locales

# Установите локали
RUN locale-gen en_US.UTF-8 ru_RU.UTF-8
ENV LANG en_US.UTF-8
ENV LC_ALL ru_RU.UTF-8

# Добавьте остальные инструкции для вашего приложения