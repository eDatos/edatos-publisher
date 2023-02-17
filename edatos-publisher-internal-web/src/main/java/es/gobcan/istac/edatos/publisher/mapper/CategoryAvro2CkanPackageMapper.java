package es.gobcan.istac.edatos.publisher.mapper;

import es.gobcan.istac.edatos.publisher.core.ckan.dto.*;
import org.siemac.metamac.srm.core.stream.message.CategorySchemeAvro;
import org.siemac.metamac.srm.core.stream.message.LocalisedStringAvro;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class CategoryAvro2CkanPackageMapper {

    public CkanPackage toCkanPackage(CategorySchemeAvro category) {
        LocalisedStringAvro name = category.getName().getLocalisedStrings().stream().filter(locString -> "es".equals(locString.getLocale())).findFirst().orElse(null);

        // TODO EDATOS-3460
        CkanPackage newPackage = new CkanPackage();
        newPackage.setName("clasificacion_tematica_de_operaciones_estadisticas_en_canarias-2104");
        newPackage.setTitle(category.getName().getLocalisedStrings().stream().filter(x -> x.getLocale().equals("es")).findFirst().get().getLabel());
        newPackage.setPrivates(false);
        newPackage.setAuthor("Jorge");
        newPackage.setAuthor_email("arte@correo.com");
        newPackage.setMaintainer("Aitor");
        newPackage.setMaintainer_email("Aitormenta@bromasdechiquillos.com");
        newPackage.setLicense_id("PDDL-1.0");
        newPackage.setNotes(category.getDescription().getLocalisedStrings().stream().filter(locString -> "es".equals(locString.getLocale())).findFirst().map(LocalisedStringAvro::getLabel).orElse(""));
        newPackage.setUrl("https://docs.ckan.org/en/latest/api/index.html#ckan.logic.action.create.package_create");
        newPackage.setVersion("01.000");
        newPackage.setState("active");
        //newPackage.setType();
        newPackage.setResources(new ArrayList<>());
        newPackage.getResources().add(new CkanResource(newPackage.getName(), "https://www.stats.govt.nz/assets/Uploads/Annual-enterprise-survey/Annual-enterprise-survey-2020-financial-year-provisional/Download-data/annual-enterprise-survey-2020-financial-year-provisional-csv.csv", "Prueba recurso"));
        newPackage.setTags(new ArrayList<>());
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().add(new CkanExtra("urn", category.getUrn()));
        newPackage.setRelationships_as_object(new ArrayList<>());
        newPackage.setRelationships_as_subject(new ArrayList<>());
        newPackage.setGroups(new ArrayList<>());
        newPackage.setOwner_org("istac");
        /*newPackage.setName(name.getLabel().toLowerCase().replace(" ", "_").replaceAll("[^\\w_-]", "") + String.valueOf(ThreadLocalRandom.current().nextInt(1, 1000000000 + 1)));
        newPackage.setTitle(name.getLabel());
        newPackage.setPrivates();
        newPackage.setAuthor("Paco");
        newPackage.setAuthor_email("Pacomerte@bromasdechiquillos.com");
        newPackage.setMaintainer("Aitor");
        newPackage.setMaintainer_email("Aitormenta@bromasdechiquillos.com");
        newPackage.setLicense_id("istac-aviso-legal");
        newPackage.setNotes(category.getDescription().getLocalisedStrings().stream().filter(locString -> "es".equals(locString.getLocale())).findFirst().map(LocalisedStringAvro::getLabel).orElse(""));
        newPackage.setUrl("https://downloadmoreram.com/");
        newPackage.setVersion(String.valueOf(category.getVersion()));
        newPackage.setState();
        newPackage.setType();
        newPackage.setResources(new ArrayList<>());
        newPackage.setTags(new ArrayList<>());
        newPackage.getTags().add(new CkanTag("2017"));
        newPackage.setExtras(new ArrayList<>());
        newPackage.getExtras().add(new CkanExtra("urn", category.getUrn().replaceFirst("\\([^)]*\\)$", "")));
        newPackage.setRelationships_as_object();
        newPackage.setRelationships_as_subject();
        newPackage.setGroups(new ArrayList<>());
        newPackage.getGroups().add(new CkanGroup("deporte"));
        newPackage.setOwner_org("istac");*/

        return newPackage;
    }

    public CkanPackage toCkanPackage2(CategorySchemeAvro category) {
        LocalisedStringAvro name = category.getName().getLocalisedStrings().stream().filter(locString -> "es".equals(locString.getLocale())).findFirst().orElse(null);

        // TODO EDATOS-3460
        CkanPackage newPackage = new CkanPackage();
        /*newPackage.setName(category.getName()); // TODO: choose language
        newPackage.setTitle(); // TODO: choose, same as name?
        newPackage.setPrivates(false); // TODO
        newPackage.setAuthor(category.get); // TODO
        newPackage.setAuthor_email(); // TODO
        newPackage.setMaintainer(category.getMaintainer().getTitle()); // TODO, what property?
        newPackage.setMaintainer_email(category.getMaintainer()); // TODO, what email?
        newPackage.setLicense_id(); // TODO
        newPackage.setNotes(category.getDescription()); // TODO
        newPackage.setUrl(); // TODO
        newPackage.setVersion(String.valueOf(category.getVersion()));
        newPackage.setState("active"); // TODO, always active?
        newPackage.setType();
        newPackage.setResources(new ArrayList<>());
        newPackage.setTags(new ArrayList<>());
        newPackage.setExtras(new ArrayList<>());
        newPackage.setRelationships_as_object();
        newPackage.setRelationships_as_subject();
        newPackage.setGroups(new ArrayList<>());
        newPackage.setOwner_org();*/

        return newPackage;
    }
}
