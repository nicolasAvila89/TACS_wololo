package tacs.wololo.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tacs.wololo.model.DTOs.GameInfoDto;
import tacs.wololo.model.DTOs.GameStatusDto;
import tacs.wololo.model.DTOs.MunicipalityDto;
import tacs.wololo.model.Map;
import tacs.wololo.model.*;
import tacs.wololo.repositories.GameRepository;
import tacs.wololo.repositories.UserRepository;
import tacs.wololo.services.IGameService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameService implements IGameService {

    @Autowired
    GameRepository gameRepository;

    @Autowired
    UserRepository userRepository;

    public GameService() {

    }

    public void createGame(GameInfoDto gameInfoDto)
    {
        List<String> playersUsernames = gameInfoDto.getPlayersUsernames();
        Collections.shuffle(playersUsernames);

        Queue<Player> players = new LinkedList<>();

        for(String username : playersUsernames)
        {
            Player player = new Player(username);
            players.add(player);
        }

        Game game = new Game(new Map(), gameInfoDto.getProvinceName(), new Date(),
                players, GameState.CREATED, gameInfoDto.getMunicipalitiesCant());

        gameRepository.addGame(game);
    }

    public List<GameInfoDto> getGames(String username)
    {
        System.out.println("Cambia algo");
        List<Game> games = gameRepository.getGames(username);

        if(games.isEmpty())
        {
            System.out.println("Estoy vacio");
            return new ArrayList<>();
        }


        List<GameInfoDto> gameInfoDtos = new ArrayList<>();

        for(Game game : games)
        {
            List<String> usernames = new ArrayList<Player>(game.getPlayers())
                    .stream().map(Player::getUsername).collect(Collectors.toList());

            gameInfoDtos.add(new GameInfoDto(usernames, game.getProvince(), game.getMunicipalityLimit()
                    , game.getId()));
        }

        return gameInfoDtos;
    }

    public GameStatusDto getGame(Long gameId, String username)
    {
        Game game = gameRepository.getGame(gameId, username);

        List<String> playersNames = new ArrayList<>();

        List<MunicipalityDto> municipalityDtos = new ArrayList<>();

        for(Player player : game.getPlayers())
        {
            playersNames.add(player.getUsername());
        }

        for(Municipality municipality : game.getMunicipalities())
        {
            municipalityDtos.add(new MunicipalityDto(municipality.getCentroide(),
                    municipality.getId(), municipality.getNombre(), municipality.getGauchos(),
                    municipality.getHeight(), municipality.getMode(),
                    municipality.getOwner().getUsername(), municipality.getMovements()));
        }

        return new GameStatusDto(game.getId(), game.getProvince(), game.getDate()
        , playersNames, game.getState(), municipalityDtos, game.getMunicipalityLimit());
    }

    public int processAttack(String username, Long gameId, String attackMun, String defenceMun)
    {
        Game game = gameRepository.getGame(gameId, username);

        Municipality attack = game.getMunicipality(attackMun);
        Municipality defence = game.getMunicipality(defenceMun);

        if(attack == null || defence == null)
            return -1;

        if(attack.attackMunicipality(defence, game.getMap()))
            return 1;

        return 0;
    }

    public List<Movement> getMovementsBy(Long idGame, String username, String idMunicipality)
    {
        Game game = gameRepository.getGame(idGame, username);

        if(game == null)
            return null; // FIXME, puse esto pero no se si es una buena idea

        Municipality municipality = game.getMunicipality(idMunicipality);

        if(municipality == null)
            return null; // FIXME, puse esto pero no se si es una buena idea


        // TODO: SACAR. Dejo el comentario igual para testear, pero sacar para la entrega.
        // --------
        /*
        List<Movement> movements = new ArrayList<>();
        movements.add(new MovementDefend(10, "Chaco", true));
        movements.add(new MovementProduce(20, 5));

        municipality.setMovements(movements);*/
        // --------

        return municipality.getMovements();
    }

}
