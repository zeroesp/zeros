package com.zero.zeros.common.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zero.zeros.common.dao.CommonMapper;

@Service("commonService")
@Transactional
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	private CommonMapper commonMapper;

	@Override
	public List<HashMap<String, String>> selectMenuList() {
		return commonMapper.selectMenuList();
	}

}
