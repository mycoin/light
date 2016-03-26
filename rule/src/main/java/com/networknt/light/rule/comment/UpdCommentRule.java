package com.networknt.light.rule.comment;

import com.networknt.light.rule.Rule;
import com.networknt.light.server.DbService;
import com.networknt.light.util.ServiceLocator;
import com.tinkerpop.blueprints.impls.orient.OrientGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import org.owasp.encoder.Encode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Created by steve on 21/03/15.
 *
 * only the comment content can be updated
 *
 * Access Level [owner, admin, forumAdmin, newsAdmin, blogAdmin, user]
 *
 * now only owner and admin can update
 *
 */
public class UpdCommentRule extends AbstractCommentRule implements Rule {
    static final Logger logger = LoggerFactory.getLogger(UpdCommentRule.class);

    public boolean execute (Object ...objects) throws Exception {
        Map<String, Object> inputMap = (Map<String, Object>)objects[0];
        Map<String, Object> data = (Map<String, Object>)inputMap.get("data");
        Map<String, Object> user = (Map<String, Object>) inputMap.get("user");
        String host = (String)data.get("host");
        String rid = (String)data.get("@rid");
        String entityRid = (String)data.get("entityRid");
        String error = null;
        OrientGraph graph = ServiceLocator.getInstance().getGraph();
        try {
            // check if the comment exists
            OrientVertex comment = (OrientVertex) DbService.getVertexByRid(graph, rid);
            if(comment == null ) {
                error = "Comment @rid " + rid + " cannot be found";
                inputMap.put("responseCode", 404);
            } else {
                Map eventMap = getEventMap(inputMap);
                Map<String, Object> eventData = (Map<String, Object>)eventMap.get("data");
                inputMap.put("eventMap", eventMap);
                eventData.put("commentId", comment.getProperty("commentId"));
                eventData.put("content", Encode.forJavaScriptSource((String)data.get("content")));
                clearCommentCache(entityRid);
            }
        } catch (Exception e) {
            logger.error("Exception:", e);
            throw e;
        } finally {
            graph.shutdown();
        }
        if(error != null) {
            inputMap.put("result", error);
            return false;
        } else {
            return true;
        }
    }
}
