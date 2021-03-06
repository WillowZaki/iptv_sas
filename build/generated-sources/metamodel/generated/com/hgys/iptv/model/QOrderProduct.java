package com.hgys.iptv.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QOrderProduct is a Querydsl query type for OrderProduct
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrderProduct extends EntityPathBase<OrderProduct> {

    private static final long serialVersionUID = 618949475L;

    public static final QOrderProduct orderProduct = new QOrderProduct("orderProduct");

    public final StringPath code = createString("code");

    public final StringPath col1 = createString("col1");

    public final NumberPath<Integer> col2 = createNumber("col2", Integer.class);

    public final StringPath col3 = createString("col3");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final DateTimePath<java.sql.Timestamp> inputTime = createDateTime("inputTime", java.sql.Timestamp.class);

    public final NumberPath<Integer> isdelete = createNumber("isdelete", Integer.class);

    public final NumberPath<Integer> mode = createNumber("mode", Integer.class);

    public final DateTimePath<java.sql.Timestamp> modifyTime = createDateTime("modifyTime", java.sql.Timestamp.class);

    public final StringPath name = createString("name");

    public final StringPath note = createString("note");

    public final StringPath scdcode = createString("scdcode");

    public final StringPath scdname = createString("scdname");

    public final StringPath sdcode = createString("sdcode");

    public final StringPath sdname = createString("sdname");

    public final NumberPath<Integer> status = createNumber("status", Integer.class);

    public QOrderProduct(String variable) {
        super(OrderProduct.class, forVariable(variable));
    }

    public QOrderProduct(Path<? extends OrderProduct> path) {
        super(path.getType(), path.getMetadata());
    }

    public QOrderProduct(PathMetadata metadata) {
        super(OrderProduct.class, metadata);
    }

}

