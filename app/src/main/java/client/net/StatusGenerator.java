package client.net;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import model.domain.Status;

/*
    Reading from a local JSON file is not going to work.
    I think it's because of the Android Emulator--it can't read from the host filesystem.
 */

public class StatusGenerator {
    private static StatusGenerator instance;
    private static File jsonFile;

    private StatusGenerator() {}
    public static StatusGenerator getInstance() {
        if (instance == null) {
            instance = new StatusGenerator();
        }
        return instance;
    }

    private static Status[] parseJsonToStatusArray(File file) throws IOException
    {
        List<Status> statusList = new ArrayList<>();
        try (BufferedReader fileReader = new BufferedReader(new FileReader(file)))
        {
            JsonReader jsonReader = new JsonReader(fileReader);

            jsonReader.beginArray();
            while (jsonReader.hasNext()) {
                String msg = new Gson().fromJson(jsonReader, String.class);
                Status status = new Status();
                status.setMessage(msg);
                statusList.add(status);
            }
        }
        catch (IOException e)
        {
            throw e;
        }

        Object[] tmpArr = statusList.toArray();
        Status[] statusArr = Arrays.copyOf(tmpArr, tmpArr.length, Status[].class);
        return statusArr;
    }

    public List<Status> generateStatuses(int count) throws Exception {
        List<Status> genStatuses = new ArrayList<>();

        Random random = new Random();

        while (genStatuses.size() < count) {
            Status status = new Status();
            String msg = dummyStrings[random.nextInt(dummyStrings.length)];
            status.setMessage(msg);
            //status.setRandomDate();
            status.setMilliseconds();
            status.findMentionsInStatus();
            /* Shouldn't need line below because of android:autoLink="web" in layout */
            //status.findUrlsInStatus();
            genStatuses.add(status);
        }

        return genStatuses;
    }

    private static final String[] dummyStrings = {
        "If you haven't seen Sonic yet, it's very good and worth seeing!",
        "Been a tough day. Haggen Daz is a lifesaver.",
        "omg Chris Hemwsorth is bae",
        "Is this a JoJo's Reference? https://i.kym-cdn.com/photos/images/newsfeed/001/217/242/1d4.png",
        "I like gravy",
        "Carol drank the blood as if she were a vampire.",
        "He was sitting in a trash can with high street class.",
        "When transplanting seedlings, candied teapots will make the task easier.",
        "You realize you're not alone as you sit in your bedroom massaging your calves after a long day of playing tug-of-war with Grandpa Joe in the hospital.",
        "I am my aunt's sister's daughter.",
        "The crowd yells and screams for more memes.",
        "Baby Yoda meme https://static.boredpanda.com/blog/wp-content/uploads/2019/11/1197339563775209472-png__700.jpg",
        "My feelings coding PM #2 for CS340 https://mamasgeeky.com/wp-content/uploads/2019/11/baby-yoda-meme-11.jpg",
        "More like 'My face if I get a 100 on PM #2' https://walyou.com/wp-content/uploads//2019/12/star-wars-baby-yoda-meme-1024x956.png",
        "Abstraction is often one floor above you.",
        "The waitress was not amused when he ordered green eggs and ham.",
        "This is a Japanese doll. https://i.ytimg.com/vi/n-aZBwOj94w/maxresdefault.jpg",
        "Yo let's try this place out https://www.lucalibrooklyn.com/brooklyn-menu/",
        "I can relate lol https://i.imgflip.com/mudbw.jpg",
        "Who wants to go roller blading tonight?",
        "Tried to fart silently today. People in the next room heard it.",
        "Here is my unwanted political opinion...",
        "Send me cat videos!",
        "Here's my 6th baby post of the day!",
        "Ugh I can't even with this",
        "Facts don't care about your feelings",
        "Don't forget to eat your veggies, wash behind your ears, don't talk to strangers on the internet, and that Jeffrey Epstein didn't kill himself.",
        "Just another fake status for PM #2 lol",
        "Dis you https://i.imgflip.com/3plh3h.jpg",
        "I hope CS340 isn't like this https://i.imgflip.com/3p7aah.jpg",
        "I’m working on a sweet potato farm.",
        "We had a three-course meal.",
        "It turns out you don't need all that stuff you insisted you did.",
        "I love Elder Holland!",
        "People who insist on picking their teeth with their elbows are so annoying!",
        "I want more detailed information.",
        "We need to rent a room for our party.",
        "Everyone was busy, so I went to the movie alone.",
        "He decided water-skiing on a frozen lake wasn’t a good idea.",
        "It's not easy coming up with fake dummyStrings for PM #2",
        "What's the chance that the TA sees this status while grading my PM #2?",
        "Iguanas were falling out of the trees.",
        "The trick to getting kids to eat anything is to put ketchup on it. #momtips",
        "It didn't make sense unless you had the power to eat colors.",
        "Brad came to dinner with us. So nice to see an old friend.",
        "His seven-layer cake only had six layers.",
        "Yare Yare Daze",
        "The Dragon Prince is probably better than Avatar the Last Airbender, just saying",
        "Guys who eat boneless wings close the fridge door with their hips",
        "That guy that's tailing you too closely in ihs big truck is just getting home as fast as he can so he can kiss his dad on the lips",
        "I had a Karen experience while working customer service today",
        "Jerky is just cow raisins. Change my mind.",
        "I just can't stand this politician running for President!",
        "If you can't handle me at my worst then you don't deserve me at my best",
        "I don't always make Android apps, but when I do, I'm happy that it isn't Angular",
        "sudo A >> grades.txt",
        "What if this reality isn't reality, that we're actually fake people in somebody's school app assignment?",
        "Stone Ocean's ending had me like wut",
        "All's well that ends well",
        "I just saw Jerruba walk by",
        "Can we just agree that Disney Star Wars isn't canon?",
        "Add me on Pokemon Go, my trainer code is 4572 9059 1272 8041",
        "The guy across from me said he made a Google Doc",
        "There's a Po statue in the Talmage!",
        "How many CS students actually spend all their free time doing Leetcode and software dev?",
        "Well, this is the best I could do",
        "How very original",
        "What does this mean exactly, for BYU? https://www.deseret.com/opinion/2020/2/19/21144720/byu-honor-code-gay-homosexual-behavior-lgbtq-agency",
        "Kirsten Dunst has been successful off camera. Been wondering what happened to her. https://getpocket.com/explore/item/in-praise-of-kirsten-dunst-the-most-underrated-actor-of-her-generation?utm_source=pocket-newtab",
        "https://getpocket.com/explore/item/how-close-are-we-really-to-building-a-quantum-computer?utm_source=pocket-newtab",
        "You don't know me",
        "I'm only 26 yet I have to use Urban Dictionary to understand what the kids are saying on Instagram",
        "Apparently the computer scientist that came up with cut/copy/paste passed away recently",
        "Yall women must be wanting expensive Valentine's gifts cuz I haven't seen any men are trash posts lately",
        "oh you don't want tomatoes on your burger? do you also want a diaper change? maybe some milk? since you're acting like a child",
        "If I won 10mil from the Powerball, I'd pay off my student loans and use the remaining $10 to go to the movies",
        "If yall gone say your child is 48 months yall better catch these hands",
        "the Guitar Hero audience booing when youre doing poorly is litterally the worst feeling in the world",
        "Think your cool huh",
        "How many poorly spelled dummyStrings do I have to read today???",
        "mom can you pick me up? im at a party and theres someone funnier than me",
        "When you lie on the application but still get the job",
        "I would've started saving my money in 5th grade if I knew bills were like this",
        "When you finally give your body a celery stick after days of Hot Pockets and Mountain Dew. Health.",
        "If a dog draws an Uno card that says 'show me what's in your mouth or draw 25', he's drawing 25",
        "What the heck does bomboclat mean? #millenialbutold",
        "2nite @ work a customer: ordered 3 meatballs, ate them all, ordered 3 more meatballs, ate those too. Told his server to take empty plates b4 his wife got there. His wife gets there. server takes both of their orders. He goes 'I think I'll try the meatballs' WHAT WAS HE HIDING",
        "Nothing fits a dirty gross place like Chuck E Cheese than a gray rat for a mascot",
        "I was born ready for mango season",
        "49ers fans cried in the arms of their dads this month",
        "Remove the first and last letter of you name. Let's see how cool your new name is.",
        "I hate when guys flex their height in their bios...makes me six two my stomach",
        "normal person: 9+7=16. me: if 10+7 is 17 and 9 is one less than 10 then 9+7 must be 16",
        "We're in 2020 and bowling alley animations are still in 1990",
        "I'm the type of person that will restart a song because I got distracted and didn't appreciate it enough",
        "Even the cows don't like the people that order steak well done",
        "Scooby Doo is basically a show that teaches kids that the villains in our lives are usually not strangers, but rather people we know & trust",
        "If you sleep in jeans you're a psychopath",
        "Guys could have nothing but a recliner, a tv, and a PS4 in their living room and still be happy",
        "plz dont compliment me, i dont know how to react",
        "So did the US and Huawei kiss and make up or..."
    };
}
