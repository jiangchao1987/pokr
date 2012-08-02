package com.yanchuanli.games.pokr.util;

import com.yanchuanli.games.pokr.dto.PlayerDTO;
import com.yanchuanli.games.pokr.model.Player;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DTOUtil {

    public static List<PlayerDTO> getPlayerDTOList(List<Player> players) {
        List<PlayerDTO> playerDTOs = new ArrayList<>();
        for (Player aplayer : players) {
            playerDTOs.add(new PlayerDTO(aplayer));
        }
        return playerDTOs;
    }

    public static String writeValue(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}
