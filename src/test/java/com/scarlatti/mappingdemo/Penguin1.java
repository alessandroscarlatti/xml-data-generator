package com.scarlatti.mappingdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Alessandro Scarlatti
 * @since Saturday, 7/20/2019
 */
public class Penguin1 implements Cloneable {

    private String name;
    private List<Pet> pets = new ArrayList<>();
    private List<Toy> toys = new ArrayList<>();

    public Penguin1() {
    }

    public Penguin1(Penguin1 other) {
        setName(other.getName());

        if (other.getPets() != null) {
            for (Pet pet : other.getPets()) {
                getPets().add(new Pet(pet));
            }
        }

        if (other.getToys() != null) {
            for (Toy toy : other.getToys()) {
                getToys().add(new Toy(toy));
            }
        }
    }

    @Override
    public String toString() {
        return "Penguin1{" +
            "name='" + name + '\'' +
            ", pets=" + pets +
            ", toys=" + toys +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Penguin1 penguin1 = (Penguin1) o;
        return Objects.equals(name, penguin1.name) &&
            Objects.equals(pets, penguin1.pets) &&
            Objects.equals(toys, penguin1.toys);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name, pets, toys);
    }

    @Override
    protected Penguin1 clone() {
        return new Penguin1(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Pet> getPets() {
        return pets;
    }

    public void setPets(List<Pet> pets) {
        this.pets = pets;
    }

    public List<Toy> getToys() {
        return toys;
    }

    public void setToys(List<Toy> toys) {
        this.toys = toys;
    }

    public static class Pet {
        private String name;

        public Pet() {
        }

        public Pet(Pet other) {
            this.name = other.name;
        }

        @Override
        public String toString() {
            return "Pet{" +
                "name='" + name + '\'' +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pet pet = (Pet) o;
            return Objects.equals(name, pet.name);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class Toy {
        private String name;

        public Toy() {
        }

        public Toy(Toy other) {
            this.name = other.name;
        }

        @Override
        public String toString() {
            return "Toy{" +
                "name='" + name + '\'' +
                '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Toy pet = (Toy) o;
            return Objects.equals(name, pet.name);
        }

        @Override
        public int hashCode() {

            return Objects.hash(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
