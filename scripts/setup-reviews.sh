#Creación colección reviews

if [ -z "$MONGOURI" ]; then
  echo "Error: La variable MONGOURI no está definida."
  exit 1
fi
mongosh "$MONGOURI" --eval '
  db.getSiblingDB("DevHubDB").createCollection("answers");
  print("Colección reviews creada");
'
