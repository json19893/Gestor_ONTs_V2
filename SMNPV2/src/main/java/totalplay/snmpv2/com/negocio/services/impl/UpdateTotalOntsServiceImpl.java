package totalplay.snmpv2.com.negocio.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import totalplay.snmpv2.com.negocio.dto.GenericResponseDto;
import totalplay.snmpv2.com.negocio.services.IUpdateTotalOntsService;
import totalplay.snmpv2.com.persistencia.entidades.CatOltsEntity;
import totalplay.snmpv2.com.persistencia.entidades.VwTotalOntsEntity;
import totalplay.snmpv2.com.persistencia.repositorio.ITotalOntsRepository;
import totalplay.snmpv2.com.persistencia.repositorio.IcatOltsRepository;

import java.util.List;

@Service
public class UpdateTotalOntsServiceImpl implements IUpdateTotalOntsService {
    @Autowired
    ITotalOntsRepository IOntsrepository;

    @Autowired
    IcatOltsRepository IOltRepository;
    @Autowired
    MongoTemplate template;

    @Override
    public GenericResponseDto updateTotalOntsFromOlts() {
        GenericResponseDto response = new GenericResponseDto();
        response.setCod(0);
        response.setSms("Success: Se actualizo el total de onts por cada olt");

        try {
            List<VwTotalOntsEntity> collection = IOntsrepository.findAll();
            List<CatOltsEntity> catOlts = IOltRepository.findAll();

            for (CatOltsEntity catOlt : catOlts) {
                for (VwTotalOntsEntity tmpOlt : collection) {
                    //Busca la olts en el catalogo
                    if (catOlt.getId_olt().intValue() == tmpOlt.getId_olt().intValue()) {
                        //Remueve los elementos ya setteados
                        catOlt.setTotal_onts(tmpOlt.getTotal_onts() == 0 ? 0 : tmpOlt.getTotal_onts());
                        //catOlt.setTotal_onts(1000);
                    }
                }
            }
            IOltRepository.saveAll(catOlts);
        } catch (Exception ex) {
            response.setCod(0);
            response.setSms("Error: Hubo un error en el servidor");
        }
        return response;
    }
}

