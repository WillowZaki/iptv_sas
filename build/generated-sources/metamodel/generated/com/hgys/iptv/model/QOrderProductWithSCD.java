package com.hgys.iptv.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderProductWithSCD is a Querydsl query type for OrderProductWithSCD
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrderProductWithSCD extends EntityPathBase<OrderProductWithSCD> {

    private static final long serialVersionUID = 1349499019L;

    public static final QOrderProductWithSCD orderProductWithSCD = new QOrderProductWithSCD("orderProductWithSCD");

    public final DateTimePath<java.sql.Timestamp> createtime = createDateTime("createtime", java.sql.Timestamp.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath opcode = createString("opcode");

    public final StringPath opname = createString("opname");

    public final StringPath pcode = createString("pcode");

    public final StringPath pname = createString("pname");

    public QOrderProductWithSCD(String variable) {
        super(OrderProductWithSCD.class, forVariable(variable));
    }

    public QOrderProductWithSCD(Path<? extends OrderProductWithSCD> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderProductWithSCD(PathMetadata metadata) {
        super(OrderProductWithSCD.class, metadata);
    }

}

