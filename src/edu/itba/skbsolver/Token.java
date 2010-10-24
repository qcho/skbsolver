package edu.itba.skbsolver;

import java.util.EnumMap;
import java.util.Map;

public enum Token {
	WALL('#'), PLAYER('@'), PLAYERTARGET('+'), BOX('$'), BOXTARGET('*'), TARGET(
			'.'), FREE(' ');

	Map<Token, Character> map = new EnumMap<Token, Character>(Token.class);

	final public char c;

	Token(char c) {
		this.c = c;
		map.put(this, c);
	}

}
