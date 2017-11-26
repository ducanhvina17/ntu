company(sumSum).
company(appy).
competitor(sumSum, appy).
develop(sumSum, galacticaS3).
tech(galacticaS3).
boss(stevey).
stole(stevey, galacticaS3, sumSum).

rival(X) :- competitor(X, appy).
business(X) :- tech(X).
unethical(X):- boss(X), business(Y), company(Z), rival(Z), stole(X, Y, Z).