package pv260.solid.lsp.original;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestObjectsBag {

    public static class Friend {

        private final static String COMMON_PROPERTY = "IS AWESOME";

        private final String favoriteIceCream;

        private final long luckyNumber;

        private final String[] languagesSpoken;

        public Friend(String favoriteIceCream, long luckyNumber, String[] languagesSpoken) {
            this.favoriteIceCream = favoriteIceCream;
            this.luckyNumber = luckyNumber;
            this.languagesSpoken = languagesSpoken;
        }

        @Override
        public String toString() {
            return "Friend{" +
                   "favoriteIceCream=" + favoriteIceCream
                   + ", luckyNumber=" + luckyNumber
                   + ", languagesSpoken=" + Arrays.toString(languagesSpoken)
                   + '}';
        }
    }

    public static class Vehicle {

        private final int numberOfWheels;

        private final int horsepowers;

        public Vehicle(int numberOfWheels, int horsepowers) {
            this.numberOfWheels = numberOfWheels;
            this.horsepowers = horsepowers;
        }

        @Override
        public String toString() {
            return "Vehicle{"
                   + "numberOfWheels=" + numberOfWheels
                   + ", horsepowers=" + horsepowers + '}';
        }
    }

    public static class Supercar extends Vehicle{

        private final int zeroToHundredInMillis;

        private final String color;

        public Supercar(int zeroToHundredInMillis, String color, int numberOfWheels, int horsepowers) {
            super(numberOfWheels, horsepowers);
            this.zeroToHundredInMillis = zeroToHundredInMillis;
            this.color = color;
        }

        @Override
        public String toString() {
            return "Supercar{"
                    + "zeroToHundredInMillis=" + zeroToHundredInMillis
                    + ", color=" + color
                    + ", inherits "+super.toString()
                    + '}';
        }
    }

    public interface Shop<T>{

        T buy(int quantity);
    }

    public static class CarShop implements Shop<Supercar>{

        private final int leftInStock;

        public CarShop(int leftInStock) {
            this.leftInStock = leftInStock;
        }

        @Override
        public Supercar buy(int quantity) {
            throw new UnsupportedOperationException("We are closed. Sorry.");
        }

        @Override
        public String toString() {
            return "CarShop{"
                    + "leftInStock=" + leftInStock
                    + '}';
        }

    }

    public static class CarOwner{

        private final Supercar ownedCar;

        private final int topSpeedAchieved;

        private final String nicknameForCar;

        public CarOwner(Supercar ownedCar, int topSpeedAchieved, String nicknameForCar) {
            this.ownedCar = ownedCar;
            this.topSpeedAchieved = topSpeedAchieved;
            this.nicknameForCar = nicknameForCar;
        }

        @Override
        public String toString() {
            return "CarOwner{"
                    + "ownedCar=" + ownedCar
                    + ", topSpeedAchieved=" + topSpeedAchieved
                    + ", nicknameForCar=" + nicknameForCar
                    + '}';
        }
    }

    public static class BitmapPicture{

        private final String name;

        private final char[][] colorGrid;

        public BitmapPicture(final String name, final char[][] colorGrid) {
            this.name = name;
            this.colorGrid = colorGrid;
        }

        @Override
        public String toString() {
            StringBuilder picture = new StringBuilder();
            picture.append("Masterpiece ").append(name).append(System.lineSeparator());
            for(int row=0; row< colorGrid.length; row++){
                for(int col=0; col<colorGrid[row].length; col++){
                    picture.append(colorGrid[row][col]);
                }
                picture.append(System.lineSeparator());
            }
            return picture.toString();
        }

    }
}
