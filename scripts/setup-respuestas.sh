#!/bin/bash
# scripts/setup-respuestas.sh
# Crea la colección 'respuestas' en MongoDB Atlas - closes #163

if [ -z "$MONGOURI" ]; then
  echo "Error: La variable MONGOURI no está definida."
  exit 1
fi

mongosh "$MONGOURI" --eval '
  use DevHubDB;
  db.createCollection("respuestas");
  print("Colección respuestas creada");
'
