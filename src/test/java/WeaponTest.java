import it.polimi.ingsw.Cash;
import it.polimi.ingsw.Mode;
import it.polimi.ingsw.Weapon;
import org.junit.Test;

import static it.polimi.ingsw.Color.*;

public class WeaponTest {

    @Test
    public void initWeaponDescription(){
        Weapon w= new Weapon("Martello ionico", new Cash(YELLOW, 1), YELLOW);
        Mode base= new Mode("modalità base", "Dai 2 danni a 1 bersaglio nel quadrato in cui ti trovi");

        Mode alt= new Mode("modalita' polverizzare", "dai 3 danni a 1 bersaglio nel quadrato in cui ti trovi, poi muovi quel bersaglio di 0, 1, 2 quadrati in una direzione");

        w.addMode(base);
        w.addMode(alt);

        System.out.println(w.getDescription());
    }
}
