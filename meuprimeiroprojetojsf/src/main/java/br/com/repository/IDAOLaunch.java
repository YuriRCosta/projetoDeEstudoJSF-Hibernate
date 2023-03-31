package br.com.repository;

import java.util.Date;
import java.util.List;

import br.com.entity.Launch;

public interface IDAOLaunch {

	List<Launch> consult(Long codUser);

	List<Launch> consultLimit10(Long codUser);

	List<Launch> relLaunch(String numNota, Date dataInicial, Date dataFinal);
}
