package hr.fer.oprpp1.gui.calc.model;

import hr.fer.oprpp1.gui.calc.CalcValueListener;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

public class CalcModelImpl implements CalcModel {

    /*
    zastavicu koja govori je li model editabilan
    (u smislu da korisnik može dopisati novu znamenku, dodati decimalnu točku te promijeniti predznak broja)
     */
    private boolean isEditable = true;
    /*
    zastavicu koja govori je li broj pozitivan ili negativan
     */
    private boolean numberIsPositive = true;
    /*
    string varijablu koja pamti unesene znamenke
     */
    private String currentValueString = "";
    /*
    numeričku varijablu koja čuva decimalnu vrijednost usklađenu s onom iz string varijable
     */
    private double currentValue = 0.0;
    /*
    člansku varijablu tipa String koja čuva zamrznutu vrijednost prikaza
     */
    private String displayValue = null;

    /*
    Model pamti još dva podatka: aktivan operand (activeOperand) te zakazanu operaciju (pendingOperation).
     */
    private double activeOperand;
    private boolean activeOperandSet;
    private DoubleBinaryOperator pendingOperation;

    /*
    Praćenje trenutne vrijednosti naš model omogućava uporabom oblikovnog obrasca Promatrač u kojem je on
    subjekt. Promatrači su modelirani sučeljem CalcValueListener. Svaki puta kada bilo od metoda modela uzrokuje
    promjenu trenutne vrijednosti, implementacija modela mora o tome obavijestiti sve zainteresirane promatrače.
     */
    private final List<CalcValueListener> listenerList = new ArrayList<>();

    @Override
    public void addCalcValueListener(CalcValueListener l) {
        if (l == null)
            throw new NullPointerException("Added observer can't be null!");

        listenerList.add(l);
    }

    @Override
    public void removeCalcValueListener(CalcValueListener l) {
        if (l == null)
            throw new NullPointerException("Observer to be removed can't be null!");

        listenerList.remove(l);
    }

    /**
     * Pomoćna metoda koja obavještava sve prijavljene promatrače.
     */
    private void notifyCalcValueListeners() {
        for (CalcValueListener l : listenerList)
            l.valueChanged(this);
    }

    /**
     * Metoda getValue pri svakom pozivu vraća zapamćenu numeričku vrijednost (ništa ne parsira – očekuje se da je
     * zapamćena numerička vrijednost usklađena s tekstom koji se pamti).
     */
    @Override
    public double getValue() {
        return currentValue;
    }

    /**
     * Metoda setValue primljeni double zapisuje u numeričku varijablu, a primljenu vrijednost dodatno konvertira u
     * string i taj string također pamti. Pozivom ove metode model prestaje biti editabilan, tako da bi izravno nakon
     * nje poziv metode poput swapSign() trebao baciti iznimku. Primijetite da broj koji se predaje u setValue može biti
     * i pozitivno ili negativno beskonačno te NaN, u kojem slučaju se od metode toString očekuje Infinity, -Infinity
     * odnosno NaN.
     */
    @Override
    public void setValue(double value) {
        isEditable = false;

        currentValue = value;
        currentValueString = String.valueOf(value);
        freezeValue(currentValueString);
    }

    @Override
    public boolean isEditable() {
        return isEditable;
    }

    /**
     * Poziv metode clear čisti trenutnu vrijednost (ali ništa osim toga).
     */
    @Override
    public void clear() {
        currentValue = 0.0;
        currentValueString = "";
        numberIsPositive = true;
        freezeValue(null);
    }

    /**
     * Poziv metode clearAll čisti trenutnu vrijednost, aktivan operand te zakazanu operaciju.
     */
    @Override
    public void clearAll() {
        clear();
        clearActiveOperand();
        setPendingBinaryOperation(null);
    }

    /**
     * Metoda swapSign mijenja predznak zapamćenog broja. Poziv bilo koje metode kojom korisnik mijenja upisani broj
     * (promjena predznaka, unos znamenke, unos točke)automatski uklanja zamrznutu vrijednost. Ova metoda u slučaju da
     * model nije editabilan ne radi nikakvu promjenu već baca iznimku CalculatorInputException.
     */
    @Override
    public void swapSign() throws CalculatorInputException {
        if (!isEditable())
            throw new CalculatorInputException("Can't swap sign while calculator model is not editable!");

        numberIsPositive = !numberIsPositive;

        if (currentValueString.equals("")) {
            notifyCalcValueListeners();
            return;
        }
        currentValueString = numberIsPositive ? currentValueString.substring(1) : "-" + currentValueString;
        currentValue = Double.parseDouble(currentValueString);
        freezeValue(currentValueString);
    }

    /**
     * Metoda insertDecimalPoint u broj upisuje decimalnu točku, ako ona ne postoji (inače baca iznimku
     * CalculatorInputException). Poziv bilo koje metode kojom korisnik mijenja upisani broj (promjena predznaka, unos
     * znamenke, unos točke) automatski uklanja zamrznutu vrijednost. Metoda swapSign mijenja predznak zapamćenog broja.
     * Ova metoda u slučaju da model nije editabilan ne radi nikakvu promjenu već baca iznimku
     * CalculatorInputException.
     */
    @Override
    public void insertDecimalPoint() throws CalculatorInputException {
        if (!isEditable())
            throw new CalculatorInputException("Can't insert decimal point while calculator model is not editable!");
        if (currentValueString.equals(""))
            throw new CalculatorInputException("Decimal point can't be the first value inserted!");
        if (currentValueString.indexOf('.') != -1)
            throw new CalculatorInputException("Value already contains decimal point!");

        currentValueString += ".";
        currentValue = Double.parseDouble(currentValueString);
        freezeValue(currentValueString);
    }

    /**
     * Metoda insertDigit ako model nije editabilan baca iznimku CalculatorInputException; ako je model editabilan, u
     * string varijablu na kraj lijepi poslanu znamenku, ali samo u slučaju time nastaje string koji se i dalje može
     * isparsirati u konačni decimalni broj i tu vrijednost također pamti u varijabli koja čuva numeričku vrijednost;
     * ako to nije slučaj, pohranjeni string se ne mijenja, a metoda treba baciti iznimku CalculatorInputException.
     * Poziv bilo koje metode kojom korisnik mijenja upisani broj (promjena predznaka, unos znamenke, unos točke)
     * automatski uklanja zamrznutu vrijednost.
     */
    @Override
    public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
        if (!isEditable())
            throw new CalculatorInputException("Can't insert digit, model is not editable right now!");

        if (!hasFrozenValue()) {
            currentValueString = String.valueOf(digit);
        } else {
            if (currentValueString.equals("0")) {
                currentValueString = String.valueOf(digit);
            } else {
                currentValueString += digit;
            }
        }
        if (!Double.isFinite(Double.parseDouble(currentValueString)))
            throw new CalculatorInputException("Inserted double is not finite!");
        currentValue = Double.parseDouble(currentValueString);
        freezeValue(currentValueString);
    }

    @Override
    public boolean isActiveOperandSet() {
        return activeOperandSet;
    }

    @Override
    public double getActiveOperand() throws IllegalStateException {
        if (!isActiveOperandSet())
            throw new IllegalStateException("Active operand is not set!");

        return activeOperand;
    }

    @Override
    public void setActiveOperand(double activeOperand) {
        this.activeOperand = activeOperand;
        activeOperandSet = true;
    }

    @Override
    public void clearActiveOperand() {
        activeOperand = 0.0;
        activeOperandSet = false;
    }

    @Override
    public DoubleBinaryOperator getPendingBinaryOperation() {
        return pendingOperation;
    }

    @Override
    public void setPendingBinaryOperation(DoubleBinaryOperator op) {
        this.pendingOperation = op;
    }

    /**
     * Ako naklikate 3, 2, *, 2, + 1, =, na ekranu će se prikazati redom, “3”, “32”, “32” (jer ćete trenutno prikazani
     * broj zamrznuti, a sustav pamti operaciju *), “2” (jer je slanjem znamenke briše zamrznuti prikaz pa se prikazuje
     * trenutni broj), “64” (jer se rezultat zamrzava za prikaz a interno se trenutni broj postavlja na 0 te sustav
     * pamti operaciju +), “1” (jer se slanjem znamenke odmrzava prikaz), “65”.
     */
    @Override
    public void freezeValue(String value) {
        displayValue = value;
        notifyCalcValueListeners();
    }

    /**
     * Ako kalkulator ima postavljenu zamrznutu vrijednost, klik na gumb koji odgovara unarnoj ili binarnoj operaciji
     * treba baciti iznimku CalculatorInputException (primijetite da model kalkulatora ne zna za gumbe – ovo ćete
     * implementirati izravno u kodu GUI-ja).
     */
    @Override
    public boolean hasFrozenValue() {
        return displayValue != null;
    }

    // TODO Add stack features

    /**
     * Metoda toString, ako postoji zamrznuta vrijednost (nije null) za prikaz (metoda freezeValue!), vraća tu
     * vrijednost. U suprotnom vraća “0” ili “-0” ako je zapamćeni string prazan (i ovisno o predznaku), a inače vraća
     * zapamćeni string uz prikladan predznak.
     */
    @Override
    public String toString() {
        if (hasFrozenValue())
            return displayValue;

        if (currentValueString.equals(""))
            return numberIsPositive ? "0" : "-0";

        if (currentValue == Double.NaN)
            return "NaN";
        if (currentValue == Double.POSITIVE_INFINITY)
            return "Infinity";
        if (currentValue == Double.NEGATIVE_INFINITY)
            return "-Infinity";

        return currentValueString;
    }
}
