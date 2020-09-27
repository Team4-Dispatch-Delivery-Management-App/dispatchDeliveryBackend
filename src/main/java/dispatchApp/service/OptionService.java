package dispatchApp.service;

import dispatchApp.dao.OptionDao;
import dispatchApp.model.Option;

public class OptionService {
	private OptionDao optionDao;
	public Option getOptionById(int optionId) {
		return optionDao.getOptionById(optionId);
	}
}
