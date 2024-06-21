package festival.persistance.DBImplementation;

import festival.model.Angajat;
import festival.model.Validators.AngajatValidator;
import festival.persistance.Interfaces.AngajatRepository;
import festival.persistance.Utils.DBUtils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class DBAngajatHibRepository implements AngajatRepository {
    private DBUtils dbUtils;
    private static final Logger logger= LogManager.getLogger();
    private static SessionFactory sessionFactory;
    public DBAngajatHibRepository(){

    }
    AngajatValidator validator;
    public DBAngajatHibRepository(Properties properties, AngajatValidator validator)
    {
        logger.info("Initializing AngajatRepositoryDatabase with the properties given");
        dbUtils=new DBUtils(properties);
        this.validator=validator;
    }

    public static SessionFactory getSession() {
        logger.traceEntry();
        try {
            if (sessionFactory == null || sessionFactory.isClosed())
                sessionFactory = getNewSession();
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error DB " + e);
        }
        logger.traceExit(sessionFactory);
        return sessionFactory;
    }

    public static SessionFactory getNewSession() {
        SessionFactory ses = null;
        try {
            final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                    .configure() // configures settings from hibernate.cfg.xml
                    .build();
            ses = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            logger.error(e);
            System.out.println("Error getting connection " + e);
        }
        return ses;
    }


    public static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            System.err.println("Exception " + e);
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static void close() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }
    @Override
    public Angajat findByEmail(String email) {
        List<Angajat> angajati=(List<Angajat>)findAllUtilitary(List.of("email_angajat"),List.of(email));
        if(angajati!=null)
        {
            if(angajati.size()==1)
            {
                return angajati.get(0);
            }
        }
        return null;
    }

    @Override
    public Angajat findByUsername(String username) {
        List<Angajat> angajati=(List<Angajat>)findAllUtilitary(List.of("username"),List.of(username));
        if(angajati!=null)
        {
            if(angajati.size()==1)
            {
                return angajati.get(0);
            }
        }
        return null;
    }

    @Override
    public Angajat findOne(Long aLong) {
        List<Angajat> angajati=(List<Angajat>)findAllUtilitary(List.of("id_angajat"),List.of(aLong));
        if(angajati!=null)
        {
            if(angajati.size()==1)
            {
                return angajati.get(0);
            }
        }
        return null;
    }
    private Iterable<Angajat> findAllUtilitary(List<String> attributes, List<Object> values)
    {
        logger.traceEntry("Finding elements by some specific attributtes");
        SessionFactory sessionFactory=getSession();
        try(Session session= sessionFactory.openSession())
        {
            if(attributes.size()!=values.size())
            {
                logger.error("The number of attributes is different from the number of values");
                System.out.println("The number of attributes is different from the number of values");
                return null;
            }
            String query="";
            Transaction transact=null;
            try{
                transact=session.beginTransaction();
                if(attributes.size()==0)
                {
                    query="from Angajat";
                    org.hibernate.query.Query<Angajat> query1=session.createQuery(query);
                    List<Angajat> angajati=query1.list();
                    transact.commit();
                    return angajati;
                }
                query="from Angajat where ";
                for(int i=0;i<attributes.size();i++)
                {
                    query+=attributes.get(i)+"=:"+attributes.get(i);
                    if(i!=attributes.size()-1)
                    {
                        query+=" and ";
                    }
                }
                org.hibernate.query.Query<Angajat> query1=session.createQuery(query);
                for(int i=0;i<attributes.size();i++)
                {
                    query1.setParameter(attributes.get(i),values.get(i));
                }
                List<Angajat> angajati=query1.list();
                transact.commit();
                return angajati;
            }
            catch (RuntimeException e)
            {
                if(transact!=null)
                {
                    transact.rollback();
                }
                logger.error(e);
                System.out.println("Error finding all elements DB"+ e);

            }
        }
        catch (Exception e)
        {
            logger.error(e);
            System.out.println("Error finding all elements DB"+ e);
        }
        return null;
    }
    @Override
    public Iterable<Angajat> findAll() {
        return findAllUtilitary(List.of(),List.of());
    }

    @Override
    public Angajat save(Angajat entity) {
        SessionFactory sessionFactory=getSession();
        logger.traceEntry("Saving Angajat {}",entity);
        try(Session session=sessionFactory.openSession())
        {
            Transaction transact=null;
            try{
                transact=session.beginTransaction();
                session.save(entity);
                transact.commit();

            }
            catch(RuntimeException e)
            {
                if(transact!=null)
                {
                    transact.rollback();
                }
                logger.error(e);
                System.out.println("Error saving element DB"+ e);
                return entity;
            }
            return entity;
    }}

    @Override
    public Angajat delete(Long aLong) {

        if(aLong==null)
        {
            logger.error("Id-ul nu poate fi null");
            System.out.println("Id-ul nu poate fi null");
            return null;
        }

       logger.traceEntry("Deleting angajat with id {}",aLong);
        SessionFactory sessionFactory=getSession();
        try(Session session=sessionFactory.openSession())
        {
            Transaction transact=null;
            try{
                transact=session.beginTransaction();
                String query="delete from Angajat where id=:id";
                Query query1=session.createQuery(query);
                query1.setParameter("id",aLong);
                int result=query1.executeUpdate ();
                transact.commit();
                if(result==0)
                {
                    logger.traceExit("Failed deletion for Angajat with id {}",aLong);
                    return null;
                }
                logger.traceExit("Deleted angajat with id {}",aLong);
                return findOne(aLong);


            }
            catch(RuntimeException e)
            {
                if(transact!=null)
                {
                    transact.rollback();
                }
                logger.error(e);
                System.out.println("Error deleting element DB"+ e);
                return null;
            }
    }}

    @Override
    public Angajat update(Long aLong, Angajat entity) {
        logger.traceEntry("Update task {} ", entity);
        if (entity == null || aLong == null) {
            logger.error("Entity sau id-ul nu pot fi null");
            System.out.println("Entity sau id-ul nu pot fi null");
            return null;
        }
        SessionFactory sessionFactory = getSession();
        try (Session session = sessionFactory.openSession()) {
            Transaction transact = null;
            try {
                transact = session.beginTransaction();
                String query = "update Angajat set nume=:nume,prenume=:prenume,email=:email,username=:username,password=:password where id=:id";
                Query query1 = session.createQuery(query);
                query1.setParameter("nume", entity.getNume());
                query1.setParameter("prenume", entity.getPrenume());
                query1.setParameter("email", entity.getEmail());
                query1.setParameter("username", entity.getUsername());
                query1.setParameter("password", entity.getPassword());
                query1.setParameter("id", aLong);
                int result = query1.executeUpdate();
                transact.commit();
                if (result == 0) {
                    logger.traceExit("Failed update for Angajat with id {}", aLong);
                    return null;
                }
                logger.traceExit("Updated angajat with id {}", aLong);
            } catch (RuntimeException e) {
                if (transact != null) {
                    transact.rollback();
                }
                logger.error(e);
                System.out.println("Error updating element in  DB" + e);
                return null;
            }
        }
        return entity;
    }
}
