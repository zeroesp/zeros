package com.zero.zeros.common.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	List<HashMap<String,String>> selectMenuList();

}
