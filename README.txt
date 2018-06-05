Program has been written in Eclipse.

On the gui you will find the input text field and a button which starts the evaluation of the input.
Above the button you will find colored labels which change color depending on the input, answering whether it fulfills the FSR, VSR and/or CSR.
Green means "yes, it is an element of this group" and red means "no".

Below the input there is a label which will be written only if the schedule was serializable and the content of the label is exactly the given schedule serialized.
Below that label there is one more label which firstly fills the schedule with locks and unlocks.
Additionally this label will also change colors depending on the allegiance of the schedule to 2pl and s2pl.
If it is green, then the schedule follows the s2pl, yellow means 2pl and red means that the schedule does not follow any 2 phase model.

Below the final label there is a panel which will draw the conflict graph. 
I used some hard-coded measurments which means that it might look off or even wrong, when the screen resolution is different than 1920x1080.
The lines of the arrows might not be always visible, that is why I randomized the sizes of the arrow heads and colors.

This program is not unbreakable and works good only if you follow the API of decoder.java. 
It does require you to insert a correct schedule, it does find some mistakes, but it is far from perfect (try to input "a").