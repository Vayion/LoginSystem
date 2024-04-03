package de.vayion.LoginSystem.playerManagement;

import de.vayion.LoginSystem.Utils;
import de.vayion.LoginSystem.idManagement.UserProfile;
import de.vayion.LoginSystem.plotManagement.Plot;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerWrapper {


    private String originalName;
    private Player player;
    private UserProfile userProfile;
    private boolean loggedIn;

    public PlayerWrapper(Player player) {
        this.player = player;
        originalName = player.getName();
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public boolean login(UserProfile userProfile) {
        if((!loggedIn)&&(userProfile.login(player))) {
            loggedIn = true;
            this.userProfile = userProfile;
            if(userProfile.getName().contains(" ")) {
                String name[] = userProfile.getName().split(" ", 2);
                Utils.changeName(name[0], player);
            }
            else {
                Utils.changeName(userProfile.getName(), player);
            }
            return true;
        }
        return false;

    }

    public boolean logout() {
        if(loggedIn) {
            loggedIn = false;
            userProfile.getiDMain().getMain().getPlotListeners().removeFromEdit(player);
            userProfile.logout(player);
            this.userProfile = null;
            Utils.changeName(originalName, player);
            return true;
        }
        return false;
    }


    public boolean isAllowed(Location loc) {
        if(!loggedIn) {return false;}
        boolean group = userProfile.getGroups().stream()
                .anyMatch(n->n.isAllowed(loc));
        return (userProfile.getPlot().isAllowed(loc)||group);
    }

    public boolean isInBorders(Location loc) {
        if(!loggedIn) {return false;}
        boolean group = userProfile.getGroups().stream()
                .anyMatch(n->n.isAllowed(loc));
        return (userProfile.getPlot().getClaimBlockPlot(loc)||group);
    }

    public boolean claimPlot(int id, Player player) {
        return userProfile.claimPlot(id);

    }

    public boolean isOwnPlot(Plot plot, Player player) {
        if(userProfile == null) {return false;}
        return plot.equals(userProfile.getPlot());
    }


    public void sendHome() {
        if(loggedIn) {
            userProfile.sendHome(player);
        }
    }

    public long getCurrency(){
        return !loggedIn?Long.MIN_VALUE: userProfile.getCurrency();
    }
    public void setCurrency(long currency) {
        if(loggedIn){
            userProfile.setCurrency(currency);
        }
    }
    public void addCurrency(long currency) {
        if(loggedIn){
            userProfile.addCurrency(currency);
        }
    }
    public void subtractCurrency(long currency) {
        if(loggedIn){
            userProfile.subtractCurrency(currency);
        }
    }

    public UserProfile getPlayerProfile(Player player) {
        return userProfile;
    }

    public String getID() {
        return loggedIn?userProfile.getID():null;
    }

}
