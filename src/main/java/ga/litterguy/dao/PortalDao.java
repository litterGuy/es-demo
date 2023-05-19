package ga.litterguy.dao;

import ga.litterguy.domain.PortalNews;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortalDao extends ElasticsearchRepository<PortalNews, String> {

}
