#Creación colección answers

if [ -z "$MONGOURI" ]; then
  echo "Error: La variable MONGOURI no está definida."
  exit 1
fi
mongosh "$MONGOURI" --eval '
  use DevHubDB;
  db.createCollection("answers");
  print("Colección answers creada");
'
