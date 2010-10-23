package edu.itba.skbsolver;

import java.util.Map;
import java.util.EnumMap;


public enum Token {
	WALL('#'),
	PLAYER('@'),
	PLAYERTARGET('+'),
	BOX('$'),
	BOXTARGET('*'),
	TARGET('.'),
	FREE(' ');
	
	static Map<Token, Character> map = new EnumMap<Token, Character>(Token.class);
	
	Token(char c){
		map.put(c, this);
	}
	
}
