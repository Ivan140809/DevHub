

@Service
//aqui se manejara la logica de negocio, se llama a los repositorios 
//y se hacen las operaciones necesarias para cumplir con los 
//requerimientos de la aplicacion
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    //crear usuarios, listarlos, actualizarlos, eliminarlos, etc
    //metodos para manejar la logica de negocio relacionada con los usuarios
    //para esto se hace uso de los metodos del repositorio, como save, findAll, findById, deleteById, etc



}