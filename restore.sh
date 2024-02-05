#!/bin/bash
sleep 1800
while true; do
  echo "inicio de restauracion automatica del sistema"
  LATEST_DUMP=$(ls -t /backup | grep .sql | head -n 1)
  mysql -h db -u${DB_USER} -p${DB_PASS} ${DB_NAME} < /backup/${LATEST_DUMP} > output.log 2> errors.log
  echo "restauracion realizada"
  sleep 3600  # espera una hora antes de ejecutar la próxima restauración
done