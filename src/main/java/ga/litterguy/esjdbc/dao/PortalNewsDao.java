package ga.litterguy.esjdbc.dao;

import ga.litterguy.domain.PortalNews;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PortalNewsDao {
    List<PortalNews> getList();
}
