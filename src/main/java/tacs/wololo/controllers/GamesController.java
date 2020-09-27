package tacs.wololo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tacs.wololo.model.DTOs.ActionDto;
import tacs.wololo.model.DTOs.GameInfoDto;
import tacs.wololo.model.Game;
import tacs.wololo.model.Movement;
import tacs.wololo.security.payload.MessageResponse;
import tacs.wololo.services.implementations.GameService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@CrossOrigin
@RequestMapping("/api/games")
public class GamesController
{
    @Autowired
    GameService gameService;

    public String getUsername()
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();

        return userDetails.getUsername();
    }

    @PostMapping(path = "")
    public ResponseEntity<?> newGame(@Valid @RequestBody GameInfoDto gameDto)
    {
        try
        {
            Game game = gameService.createGame(gameDto);

            return ResponseEntity.status(HttpStatus.CREATED).body(game);

        }catch (Exception e)
        {
          return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                  .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping(path = "")
    public ResponseEntity<?> getGames()
    {
        return ResponseEntity.ok(gameService.getGames(getUsername()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getGame(@PathVariable(value = "id") Long gameID)
    {
        try
        {
            Game game = gameService.getGame(getUsername(), gameID);
            return ResponseEntity.ok(game);

        }catch (Exception e)
        {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(path = "/{id}/moves")
    public ResponseEntity<?> moveGauchos(@PathVariable(value = "id") Long gameID,
                                         @RequestBody @Validated ActionDto actionDto)
    {
        try
        {
            Game game = gameService.moveGauchos(getUsername(), gameID, actionDto.getAttackMun(),
                    actionDto.getDefenceMun(), actionDto.getAmmount());

            return ResponseEntity.ok(game);

        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping(path = "/{id}/attacks")
    public ResponseEntity<?> attackMunicipality(@PathVariable(value = "id") Long gameID,
                                                @RequestBody @Validated ActionDto actionDto)
    {
        try
        {
            Game game = gameService.attackMunicipality(getUsername(), gameID, actionDto.getAttackMun()
                    , actionDto.getDefenceMun());

            return ResponseEntity.ok(game);

        }catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping(path = "/{id}/municipalities/mode")
    public ResponseEntity<?> changeMode(@PathVariable(value = "id") Long gameID,
                                        @RequestBody @Validated ActionDto actionDto)
    {
        try {
            Game game = gameService.changeMode(getUsername(), gameID, actionDto.getAttackMun());

            return ResponseEntity.ok(game);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping(path = "/{id}/municipality/{idMun}")
    public List<Movement> getMovements(
            @PathVariable(value = "id") Long gameID, @PathVariable(value = "idMun") Long munID)
    {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().
                getAuthentication().getPrincipal();

        List<Movement> movements = gameService.getMovementsBy(gameID, userDetails.getUsername(), munID.toString());

        return movements;
    }
}
