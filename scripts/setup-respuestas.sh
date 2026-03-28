#!/bin/bash
# scripts/setup-respuestas.sh

if [ -z "$MONGOURI" ]; then
  echo "Error: La variable MONGOURI no está definida."
  exit 1
fi

mongosh "$MONGOURI" --eval '
  db.createCollection("respuestas");
  print("Colección respuestas creada");
'
