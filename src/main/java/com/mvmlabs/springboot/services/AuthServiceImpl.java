package com.mvmlabs.springboot.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mvmlabs.springboot.entity.MyUser;

@Service
public class AuthServiceImpl implements UserDetailsService {

	@Autowired
	private MyBean myBean;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Map<String, Object> restriction  = new HashMap<String, Object>();
		restriction.put("username", "thanhtk");
		MyUser details = myBean.find(MyUser.class, restriction, true);
		Collection<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		SimpleGrantedAuthority userAuthority = new SimpleGrantedAuthority("ROLE_USER");
		SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority("ROLE_ADMIN");
		if (details.getRole().equals("user"))
			authorities.add(userAuthority);
		else if (details.getRole().equals("admin")) {
			authorities.add(userAuthority);
			authorities.add(adminAuthority);
		}
		UserDetails user = new User(details.getUsername(), details.getPassword(), true, true, true, true, authorities);
		return user;
	}

}