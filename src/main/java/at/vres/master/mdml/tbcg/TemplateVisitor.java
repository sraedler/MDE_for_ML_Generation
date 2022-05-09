package at.vres.master.mdml.tbcg;

import org.apache.velocity.runtime.parser.Token;
import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.parser.node.SimpleNode;
import org.apache.velocity.runtime.visitor.BaseVisitor;

import java.util.LinkedList;
import java.util.List;

public class TemplateVisitor extends BaseVisitor {

    @Override
    public Object visit(ASTReference node, Object data) {
        final List<Token> tokenList = new LinkedList<>();
        Token firstToken = node.getFirstToken();
        tokenList.add(firstToken);
        Token next = firstToken.next;
        while(next != null) {
            next = next.next;
            tokenList.add(next);
        }
        return tokenList;
    }

    @Override
    public Object visit(SimpleNode node, Object data) {
        return node.literal();
    }
}
