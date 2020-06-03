package dev.growi.passwordstore.server.api.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO;
import dev.growi.passwordstore.server.carddata.dao.model.CardDAO;
import dev.growi.passwordstore.server.carddata.dao.provider.CardDataProvider;
import dev.growi.passwordstore.server.carddata.domain.model.Card;
import dev.growi.passwordstore.server.shared.service.exception.CryptographyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO.*;
import static dev.growi.passwordstore.server.carddata.dao.model.CardContentDAO.ContentType.*;

@RestController
public class CardController {

    private static final Logger logger = LoggerFactory.getLogger(CardController.class);

    @Autowired
    CardDataProvider cardDataProvider;

    @RequestMapping(value = "/api/card", method = RequestMethod.GET, produces = "application/json")
    public List<Card> getAllCards(){
        return cardDataProvider.findAll().stream().map(dao -> new Card(dao)).collect(Collectors.toList());
    }

    @RequestMapping( path = "/api/card/", method = RequestMethod.POST, produces="application/json")//, consumes="application/json")
    public Card createCard( @RequestBody String json) throws IOException, CryptographyException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        logger.info(node.toString());

        Card card = Card.create(node.findValue("title").asText());
        return card;

    }

    @RequestMapping( path = "/api/card/{cardid}/createcontent", method = RequestMethod.POST )//, produces="application/json")
    public CardContentDAO createCardContent(@RequestBody String json,
                                            @PathVariable("cardid") Long cardId,
                                            @AuthenticationPrincipal UserDetails userDetails )
            throws IOException, CryptographyException {

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        logger.info(node.toString());

        CardDAO card = cardDataProvider.findById(cardId);
        ContentType contentType = valueOf(node.findValue("type").asText());

        Object content;
        switch(contentType){
            case FILE:
                content = node.findValue("content").asText();
                break;
            case TEXT:
                content = node.findValue("content").asText();
                break;
            case STRING:
                content = node.findValue("content").asText();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + contentType);
        }

        String label = node.findValue("label").asText();
        int sequenceNum = node.findValue("sequencenum").intValue();
        CardContentDAO contentDAO = cardDataProvider.createCardContent(card, contentType, content);

        return contentDAO;
    }
}
