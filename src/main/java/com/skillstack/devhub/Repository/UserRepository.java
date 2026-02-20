
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    //aqui se definen los metodos para interactuar con la base de datos, como save, findAll, findById, deleteById, etc
    //tambien se pueden definir metodos personalizados, como findByEmail, findByName
    //Esto es asi porque MongoRepository ya tiene implementados los metodos basicos para interactuar con la base de datos
    // , y al extenderlo, UserRepository hereda esos metodos y puede usarlos directamente sin necesidad de implementarlos

}