package com.willowtreeapps.examples.api;

import org.apache.http.ParseException;

import android.accounts.NetworkErrorException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import oak.http.exception.AuthenticationException;

/**
 * User: charlie Date: 1/7/13 Time: 11:43 PM
 */
public class ExampleApi {

    private static Random r = new Random();

    //Log a user in and get back a user id
    public static ApiUser LogIn(String username, String password) throws NetworkErrorException,
            ParseException {

        try
        {
            stallAndMaybeThrowException(false);
        }
        //This exception can't actually happen here
        catch (AuthenticationException ignored){}

        //If we're here then there was no exception
        ApiUser user = new ApiUser();
        user.userName = username;
        user.userID = r.nextInt();
        return user;
    }

    //Simulate uploading a file, returns the uploaded file
    public static ApiFile uploadFile(int userID, String type, String path, String date) throws NetworkErrorException, ParseException,
            AuthenticationException
    {
        stallAndMaybeThrowException(true);

        ApiFile file = new ApiFile();
        file._id = r.nextInt();
        file.type = type;
        file.date = date;
        file.url = path;
        return file;
    }

    //Simulate deleting a file, should only be used on private files
    public static boolean deleteFile(int userID, ApiFile file) throws NetworkErrorException, ParseException,
            AuthenticationException
    {
        stallAndMaybeThrowException(true);

        return true;
    }

    //Simulate getting a list of public audio files
    public static ArrayList<ApiFile> getPublicFiles() throws NetworkErrorException, ParseException
    {
        stallAndMaybeThrowException(false);

        ArrayList<ApiFile> files = new ArrayList<ApiFile>(15);
        for(int a=0; a<15; a++)
        {
            ApiFile file = new ApiFile();
            file._id = r.nextInt();
            file.type = "Public AudioRecording";
            file.date = new Date(r.nextLong()).toGMTString();
            file.url = "http://some/public/file/here/"+r.nextLong();
            files.add(file);
        }
        return files;
    }

    //Simulate getting a list of private files
    public static ArrayList<ApiFile> getPrivateFiles(int userId) throws NetworkErrorException, ParseException,
            AuthenticationException
    {
        stallAndMaybeThrowException(true);

        ArrayList<ApiFile> files = new ArrayList<ApiFile>(15);
        for(int a=0; a<15; a++)
        {
            ApiFile file = new ApiFile();
            file._id = r.nextInt();
            file.type = "Private AudioRecording";
            file.date = new Date(r.nextLong()).toGMTString();
            file.url = "http://some/private/file/here/"+r.nextLong();
            files.add(file);
        }
        return files;
    }

    //Helper method to sleep and then possibly throw an exception
    private static void stallAndMaybeThrowException(boolean throwAuthentication) throws NetworkErrorException, ParseException,
            AuthenticationException
    {
        //Sleep for a short while
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }

        //20% of the time an exception occurs
        if(r.nextInt(10)> 8)
        {
            if(!throwAuthentication)
            {
                if(r.nextBoolean())
                {
                    throw new NetworkErrorException("This was a network error");
                }
                else
                {
                    throw new ParseException("This was a parse error");
                }
            }
            else
            {
                switch(r.nextInt(3))
                {
                    case 0:
                        throw new AuthenticationException();
                    case 1:
                        throw new NetworkErrorException("This was a network error");
                    default:
                        throw new ParseException("This was a parse error");
                }
            }
        }
    }

}
