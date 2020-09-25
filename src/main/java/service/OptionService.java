package service;

import dao.OptionDao;
import model.Option;

public class OptionService {
	private OptionDao optionDao;
	public Option getOptionById(int optionId) {
		return optionDao.getOptionById(optionId);
	}
}
