package at.vres.master.mdml.tbcg;

import org.apache.velocity.runtime.parser.node.ASTReference;
import org.apache.velocity.runtime.visitor.BaseVisitor;

public class TemplateVisitor extends BaseVisitor {

    @Override
    public Object visit(ASTReference node, Object data) {
        String literal = node.literal();
        return literal.substring(2, literal.length() - 1);
    }


}
