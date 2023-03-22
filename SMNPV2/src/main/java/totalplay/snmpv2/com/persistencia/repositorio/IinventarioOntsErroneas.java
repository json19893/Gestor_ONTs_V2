package totalplay.snmpv2.com.persistencia.repositorio;

import org.springframework.data.mongodb.repository.MongoRepository;


import totalplay.snmpv2.com.persistencia.entidades.inventarioOntsErroneas;

public interface IinventarioOntsErroneas extends MongoRepository<inventarioOntsErroneas, String> {
	
}
