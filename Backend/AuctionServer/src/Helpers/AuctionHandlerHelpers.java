package Helpers;

import Handlers.AuctionHandler;
import Models.Auction;
import Models.AuctionTableRow;
import Models.Login;
import Repository.AuctionRepository;

import java.util.ArrayList;

public class AuctionHandlerHelpers {

    public static boolean ValidLogin(Login login, ArrayList<AuctionHandler> clients, AuctionHandler auctionHandler){
        clients.remove(auctionHandler);
        Main.Main.removeClient(auctionHandler);
        //get login
        Login fromRepo = auctionHandler.userRepository.getUserByUsername(login.userName);
        //if get login is not null then send true
        if(fromRepo != null && fromRepo.password.equals(login.password)){
            return true;
        }
        //else send false
        else{
            return false;
        }
    }

    public static ArrayList<Auction> GetAuctionsFromTableRows(ArrayList<AuctionTableRow> tableRows, AuctionRepository auctionRepository){

        ArrayList<Auction> auctionsToSend = new ArrayList<>();
        for(AuctionTableRow ATR: tableRows){
            auctionsToSend.add(auctionRepository.getAuctionByName(ATR.getItemName()));
        }
        return auctionsToSend;
    }
}
