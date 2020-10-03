package tacs.wololo.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tacs.wololo.model.ElementScoreBoard;
import tacs.wololo.services.implementations.StatisticsService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@PreAuthorize("hasRole('ADMIN')")
public class StatisticsController
{
    @Autowired
    StatisticsService statisticsService;

    // TODO: ver si realmente el path queda así y esta función en este controller
    // porque podría ser /games/id/socoreboard o el Controller se podría llamar AdminController
     @GetMapping(path = "/scoreboard/{id}")
     public ResponseEntity<?> scoreboard(@PathVariable(value = "id") Long gameID)
     {
         try
         {
             List<ElementScoreBoard> scoreBoard = statisticsService.getScoreBoard(gameID);
             return ResponseEntity.ok(scoreBoard);

         }catch (Exception e)
         {
             return ResponseEntity.notFound().build();
         }
     }
    @GetMapping(path = "/statistics/games")
    public ResponseEntity<?> games(){
        return ResponseEntity.ok(4);
    }

    @GetMapping(path = "/statistics/users")
    public ResponseEntity<?> users(){
        return ResponseEntity.ok(4);
    }

    // GET   estadisticas/usuarios?id=1
    @GetMapping(path = "/statistics/users/{userId}")
    public ResponseEntity<?> user(@PathVariable String userId){
        return ResponseEntity.ok(4);
    }
}
