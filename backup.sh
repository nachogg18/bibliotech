#!/bin/bash
sleep 300
while true; do
  TIMESTAMP=$(date +"%Y%m%d_%H%M%S")
  echo "inicio de creacion de backup"
  mysqldump -h db -u${DB_USER} -p${DB_PASS} ${DB_NAME} --no-tablespaces > /backup/db_backup_${TIMESTAMP}.sql
  echo "fin de backup"
  sleep 3600  # espera una hora antes de ejecutar el próximo respaldo
done