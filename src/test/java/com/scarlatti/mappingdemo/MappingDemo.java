package com.scarlatti.mappingdemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.scarlatti.mappingdemo.MappingDemo.Factory.factoryPet;
import static com.scarlatti.mappingdemo.MappingDemo.Helper.xPets;
import static com.scarlatti.mappingdemo.MappingDemo.Helper.xToys;
import static com.scarlatti.mappingdemo.MappingDemo.MapBuilder.params;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;

/**
 * @author Alessandro Scarlatti
 * @since Saturday, 7/20/2019
 */
public class MappingDemo {

    @Test
    public void testMapping() {
        Penguin1 penguin1 = new Penguin1();
        Penguin1.Pet pet1 = new Penguin1.Pet();
        pet1.setName("pet1");
        penguin1.getPets().add(pet1);

        Penguin1 penguin1b = new Penguin1();
        Penguin1.Pet pet1b = new Penguin1.Pet();
        pet1b.setName("pet1");
        penguin1b.getPets().add(pet1b);

        Penguin2 penguin2 = new Penguin2();
        Penguin2.Pet pet2 = new Penguin2.Pet();
        pet2.setName("pet2");
        penguin2.getPets().add(pet2);

        Penguin1 penguin1c = new Penguin1(penguin1);

        assertTrue(penguin1.equals(penguin1b));
        assertTrue(penguin1.equals(penguin1c));
    }

    @Test
    public void testListEquals() {
        List<String> list1 = new ArrayList<>();
        list1.add("asdf");

        List<String> list2 = new ArrayList<>();
        list2.add("asdf");

        List<String> list3 = new ArrayList<>();
        list3.add("sdfg");

        assertTrue(list1.equals(list2));
        assertFalse(list1.equals(list3));

        list2.replaceAll(s -> s + s);

        assertEquals("asdfasdf", list2.get(0));

        list2.retainAll(singletonList("asdf"));

        assertEquals(0, list2.size());

        List<String> list4 = new ArrayList<>();
    }

    @Test
    public void generateCases() {
        Penguin1 factoryPenguin = new Penguin1();
        Penguin1.Pet pet1 = new Penguin1.Pet();
        pet1.setName("pet1");
        factoryPenguin.getPets().add(pet1);


        // you'll need to wind up having some kind of factory for items.
        // This might be effectively done by contracting to have a default item provided that has
        // AT LEAST 1 instance of every value.
        // This item can now be used to instantiate a factory.
        // So you have the Factory item.
        // And you can now do "base items."?

        // so it looks like instantiating a bunch of Penguin objects, I guess.
        // then those objects can be serialized.

        // and some utility method might be able to instantiate me SEVERAL penguin objects based
        // on heuristic testing of a particular field?.
        // show which field by passing some lambda in?

        Helper helper = new Helper();
        List<Penguin1> penguins = helper.penguins(new Penguin1(), factoryPenguin);

        assertNotNull(penguins);
        for (Penguin1 penguin1 : penguins) {
            System.out.println(penguin1);
        }

        List<Penguin1> penguins2 = helper.penguinsByFactoryMethod(
            new Penguin1(),
            i -> {
                Penguin1.Pet pet = new Penguin1.Pet();
                pet.setName("pet" + i);
                return pet;
            },
            (penguin, pet) -> {
                penguin.getPets().add(pet);
            }
        );

        assertNotNull(penguins2);
        for (Penguin1 penguin1 : penguins2) {
            System.out.println(penguin1);
        }


        List<Penguin1> penguins3 = helper.penguinsByPetCount(
            new Penguin1(),
            i -> {
                Penguin1.Pet pet = new Penguin1.Pet();
                pet.setName("pet" + i);
                return pet;
            },
            (penguin, pet) -> {
                penguin.getPets().add(pet);
            },
            asList(0, 1, 2, 3, 5, 10, 15)
        );

        assertNotNull(penguins3);
        for (Penguin1 penguin1 : penguins3) {
            System.out.println(penguin1);
        }

        // how is this different than defining the cases one at a time?
        // just saving you from writing the loop?
        //
        // List<Penguin> casePenguins = penguinsByParams(
        //     params,
        //      // this could be collapsed...into a single lambda with penguin, params...
        //     params -> {
        //          // how do we build a test case from these params?
        //          factoryPenguin( penguin -> {
        //              xItems(3, "/Penguin/Pet", factory)  // assume default factory strategy?
        //              // now what would be the default strategy
        //          }
        //     }
        // )

        //  <Penguin>
        //      <Pet>
        //          <Name>asdf</Name>
        //          <Toy>
        //      </Pet>
        //      <Pet>
        //          <Name>asdf</Name>
        //      </Pet>
        //  </Penguin>

        // List<Penguin> casePenguins = penguinsByParams(
        //     params,
        //      // this could be collapsed...into a single lambda with penguin, params...
        //     factory,
        //     (penguin, params) -> {
        //         xItems(3, "/Penguin/Pet", factory)  // assume default factory strategy?
        //
        //     }
        // )

        // now what could be the default strategy?
        // could be
        // - all fields add +i
        // - some fields add +i (actually a lambda function)
        //
        // to specify SOME fields...could use xpath
        // like factory.addIncrementedField("/Penguin/Toy/Name")
        // and factory.addIncrementedField("/Penguin/Toy/Name"), (val, i) -> finalVal)
        // factory.notIncrementedField("/Penguin/Toy/Name")


        List<Penguin1> penguins4 = helper.penguinsByParams(
            asList(
                params().parm("pets", 0).parm("toys", 0).build(),
                params().parm("pets", 1).parm("toys", 1).build(),
                params().parm("pets", 2).parm("toys", 2).build(),
                params().parm("pets", 3).parm("toys", 3).build(),
                params().parm("pets", 5).parm("toys", 5).build()
            ), params -> {
                int pets = (int) ((Map) params).get("pets");
                int toys = (int) ((Map) params).get("toys");

                // now that I have the case params...
                // use utilities to generate the case
                // for example a generate x items utility for each of these params

                // generate
                // x items
                // into y thing (applicator method) // can be a utility method itself
                // from z factory (factory method)  // can be a utility method itself
                Penguin1 casePenguin = new Penguin1();


                List<Penguin1.Pet> casePets = xPets(pets, i ->
                    factoryPet(pet -> {
                            pet.setName("pet" + i);
                        }
                    ));
                for (Penguin1.Pet pet : casePets) {
                    casePenguin.getPets().add(pet);
                }

                xToys(toys, casePenguin,
                    i -> {
                        Penguin1.Toy toy = new Penguin1.Toy();
                        toy.setName("toy" + i);
                        return toy;
                    },
                    (penguin, toy) -> {
                        penguin.getToys().add(toy);
                    });

                return casePenguin;
            }
        );

        penguins4.forEach(System.out::println);
    }

    static class MapBuilder {
        private Map<String, Object> params = new HashMap<>();

        static MapBuilder params() {
            return new MapBuilder();
        }

        MapBuilder parm(String key, Object value) {
            params.put(key, value);
            return this;
        }

        Map<String, Object> build() {
            return params;
        }
    }

    static class Factory {
        static Penguin1.Pet factoryPet(Consumer<Penguin1.Pet> configurer) {
            Penguin1.Pet pet = new Penguin1.Pet();
            configurer.accept(pet);
            return pet;
        }
    }

    static class Helper {

        static List<Penguin1.Pet> xPets(int x, Function<Integer, Penguin1.Pet> petFactoryMethod) {
            List<Penguin1.Pet> pets = new ArrayList<>();
            for (int i = 0; i < x; i++) {
                pets.add(petFactoryMethod.apply(i));
            }
            return pets;
        }

        static Penguin1 xPets(int x, Penguin1 penguin, Function<Integer, Penguin1.Pet> petFactoryMethod, BiConsumer<Penguin1, Penguin1.Pet> petApplicatorMethod) {
            for (int i = 0; i < x; i++) {
                petApplicatorMethod.accept(penguin, petFactoryMethod.apply(i));
            }
            return penguin;
        }

        static Penguin1 xToys(int x, Penguin1 penguin, Function<Integer, Penguin1.Toy> toyFactoryMethod, BiConsumer<Penguin1, Penguin1.Toy> toyApplicatorMethod) {
            for (int i = 0; i < x; i++) {
                toyApplicatorMethod.accept(penguin, toyFactoryMethod.apply(i));
            }
            return penguin;
        }

        List<Penguin1> penguinsByPetCount(Penguin1 basePenguin,
                                          Function<Integer, Penguin1.Pet> factory,
                                          BiConsumer<Penguin1, Penguin1.Pet> applicator,
                                          List<Integer> counts) {
            List<Penguin1> penguins = new ArrayList<>();
            for (Integer count : counts) {
                Penguin1 penguin = new Penguin1(basePenguin);
                for (int i = 0; i < count; i++) {
                    applicator.accept(penguin, factory.apply(i));
                }
                penguins.add(penguin);
            }
            return penguins;
        }

        List<Penguin1> penguinsByParams(List<Object> paramsList, Function<Object, Penguin1> caseFactoryMethod) {
            List<Penguin1> penguins = new ArrayList<>();
            for (Object params : paramsList) {
                Penguin1 penguin = caseFactoryMethod.apply(params);
                if (penguin != null) {
                    penguins.add(penguin);
                }
            }
            return penguins;
        }

        List<Penguin1> penguins(Penguin1 basePenguin, Penguin1 factoryPenguin) {
            // create multiple pet objects
            // We want the following cases: (c for case)
            // - c0 pets
            // c1 pet
            // c2 pets
            // c3 pets
            // c5 pets
            // c10 pets

            // for now, let's make the pets identical, from the factory

            Penguin1 c0 = new Penguin1(basePenguin);  // clone from the base, assume empty pets list
            c0.setPets(new ArrayList<>());// c0 needs no additional work.

            Penguin1 c1 = new Penguin1(basePenguin);
            c1.getPets().add(new Penguin1.Pet(factoryPenguin.getPets().get(0)));  // add a new item, cloned from the factory object

            Penguin1 c2 = new Penguin1(basePenguin);
            c2.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c2.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));

            Penguin1 c3 = new Penguin1(basePenguin);
            c3.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c3.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c3.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));

            Penguin1 c5 = new Penguin1(basePenguin);
            c5.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c5.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c5.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c5.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));
            c5.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));

            Penguin1 c10 = new Penguin1(basePenguin);
            for (int i = 0; i < 10; i++) {
                c10.getPets().add((new Penguin1.Pet(factoryPenguin.getPets().get(0))));

            }

            return asList(c0, c1, c2, c3, c5, c10);
        }

        List<Penguin1> penguinsByFactoryMethod(
            Penguin1 basePenguin,
            Function<Integer, Penguin1.Pet> factory,
            BiConsumer<Penguin1, Penguin1.Pet> applicator) {

            Penguin1 c0 = new Penguin1(basePenguin);  // clone from the base, assume empty pets list
            c0.setPets(new ArrayList<>());// c0 needs no additional work.

            Penguin1 c1 = new Penguin1(basePenguin);
            applicator.accept(c1, factory.apply(0));  // add a new item, cloned from the factory object

            Penguin1 c2 = new Penguin1(basePenguin);
            applicator.accept(c2, factory.apply(0));
            applicator.accept(c2, factory.apply(1));

            Penguin1 c3 = new Penguin1(basePenguin);
            applicator.accept(c3, factory.apply(0));
            applicator.accept(c3, factory.apply(1));
            applicator.accept(c3, factory.apply(2));

            Penguin1 c5 = new Penguin1(basePenguin);
            applicator.accept(c5, factory.apply(0));
            applicator.accept(c5, factory.apply(1));
            applicator.accept(c5, factory.apply(2));
            applicator.accept(c5, factory.apply(3));
            applicator.accept(c5, factory.apply(4));

            Penguin1 c10 = new Penguin1(basePenguin);
            for (int i = 0; i < 10; i++) {
                applicator.accept(c10, factory.apply(i));
            }

            return asList(c0, c1, c2, c3, c5, c10);
        }
    }
}
