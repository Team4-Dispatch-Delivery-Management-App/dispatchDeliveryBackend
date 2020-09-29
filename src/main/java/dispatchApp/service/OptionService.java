package dispatchApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dispatchApp.dao.OptionDao;
import dispatchApp.model.Option;

@Service
public class OptionService {
	
	@Autowired
	private OptionDao optionDao;
	
	public Option getOptionById(int optionId) {
		return optionDao.getOptionById(optionId);
	}
}
