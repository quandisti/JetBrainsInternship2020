grammar SQL4Mongo;

@header {
package me.quandisti.sql4mongo;
}

query: select from where? ((limit offset?) | (offset limit?))?;

select: SELECT (any=ASTERISK | cols_list);
cols_list: column=ID (COMMA cols_list)?;

from: FROM db_name=ID;

where: WHERE pred_list;
pred_list: pred (AND pred_list)?;
pred: column=ID operator=(EQ | NEQ | LT | GT) literal=(INT | STRING);

limit: LIMIT howmuch=INT;

offset: OFFSET howmuch=INT;

/* SQL keywords */

SELECT: 'SELECT' | 'select';
FROM: 'FROM' | 'from';
WHERE: 'WHERE' | 'where';
LIMIT: 'LIMIT' | 'limit';
OFFSET: 'OFFSET' | 'offset';
AND: 'AND' | 'and';

ASTERISK: '*';
COMMA: ',';
INT: '-'?DIGIT+;
STRING: QUOTE LETTER* QUOTE;
EQ: '=';
NEQ: '<>';
LT: '<';
GT: '>';

ID: ALNUMSPEC LETTER*;
fragment QUOTE: '\'';
fragment DIGIT: [0-9];
fragment LETTER: [a-zA-Z];
/* either letter, numeric or special character */
/* added to fully support SQL naming conventions */
fragment ALNUMSPEC: [a-zA-Z0-9_#@$];

WS: [ \t]+ -> skip;
EOL: [\r\n]+ -> skip;
