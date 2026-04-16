#Creación colección comments

##export MONGOURI="    " cargar URI
if [ -z "$MONGOURI" ]; then
  echo "Error: La variable MONGOURI no está definida."
  exit 1
fi
mongosh "$MONGOURI" --eval '
  db.getSiblingDB("DevHubDB").createCollection("comments");
  print("Colección comments creada");
'

##Compilacion chmod +x setup_comments.sh && ./setup_comments.sh