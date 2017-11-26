/*
* The prolog script offers different meal options, sandwich options, meat options, salad options, sauce
* options, top-up options, sides options etc. to create a customized list of person’s choice.
* The options should be intelligently selected based on previous choices. For example,
*	If a person chose a vegan/veggie meal, meat options should not be offered.
*   If a person chose healthy meal, fatty sauces should not be offered.
*   If a person chose vegan meal, cheese top-up should not be offered.
*   If a person chose value meal, no top-up should be offered.
*   chosen_meal(X) - X is the chosen meal.
*   chosen_bread(X) - X is the chosen bread.
*   chosen_meat(X) - X is one of the chosen meat.
*   chosen_veggie(X) - X is one of the chosen veggie,
*   chosen_sauce(X) - X is one of the sauce.
*   chosen_topup(X) - X is one of the topup.
*   chosen_side(X) - X is one of the side.
*/

/*
* If H is a symbol and X is a list, then [H|X] is a list with head H and tail X.
* Set of tuples of the form (X, Y, Z), where Z consists of the elements of X followed by elements of Y.
*   ([a], [b], [a, b]) is in relation append
*   ([a], [b], []) is not
* append [] and Y to get Y.
* append [H|X] and Y to get [H|Z].
*/
append([], Y, Y).
append([H|X], Y, [H|Z]) :- append(X, Y, Z).

% List of meals, breads, main, veggies, sauce and sides for user to choose.
meals([healthy, normal, value, vegan, veggie]).
breads([flatbread, hearty_italian, italian]).
meats([bacon, salmon, tuna, turkey]).
veggies([cucumber, olive, onion, tomato]).
fatty_sauces([bbq, olive_oil, wine]).
non_fatty_sauces([chilli, ketchup]).
cheese_topups([cheddar, parmesan]).
non_cheese_topups([avocado, egg_mayo]).
sides([chips, cookies, soup]).

% Check conditions.
healthy_meal(healthy).
value_meal(value).
vegan_meal(vegan).
veggie_meal(veggie).

% Show available meals.
ask_meal(X) :- meals(X).

% Show available breads.
ask_bread(X) :- breads(X).

% Show available meats. Vegan and veggie meal has no meat.
ask_meats(X) :- chosen_meal(Y), \+vegan_meal(Y), \+veggie_meal(Y), meats(X).

% Show available veggies.
ask_veggies(X) :- veggies(X).

% Show list of sauces. Healthy meal has no fatty sauces.
ask_sauces(X) :- chosen_meal(Y), healthy_meal(Y) -> non_fatty_sauces(X) ;
	fatty_sauces(L1), non_fatty_sauces(L2), append(L1, L2, X).

%  Show list of topups. Value meal has no topup, vegan meal has no cheese topup.
ask_topups(X) :- chosen_meal(Y), \+value_meal(Y) -> (vegan_meal(Y) -> non_cheese_topups(X) ;
	cheese_topups(L1), non_cheese_topups(L2), append(L1, L2, X)).

% Show list of sides.
ask_sides(X) :- sides(X).

/*
* Show user order.
* findall(X, fact(X), L) - Find all values for fact and append it into the list L.
*/
show_meal(Meals) :- findall(X, chosen_meal(X), Meals).
show_bread(Breads) :- findall(X, chosen_bread(X), Breads).
show_meat(Meats) :- findall(X, chosen_meat(X), Meats).
show_veggie(Veggies) :- findall(X, chosen_veggie(X), Veggies).
show_sauce(Sauces) :- findall(X, chosen_sauce(X), Sauces).
show_topups(TopUps) :- findall(X, chosen_topup(X), TopUps).
show_sides(Sides) :- findall(X, chosen_side(X), Sides).
show_order(Meals, Breads, Meats, Veggies, Sauces, TopUps, Sides) :-
    show_meal(Meals), show_bread(Breads), show_meat(Meats), show_veggie(Veggies), show_sauce(Sauces),
    show_topups(TopUps), show_sides(Sides).

% When integrating with UI, the user input are saved here as facts.

