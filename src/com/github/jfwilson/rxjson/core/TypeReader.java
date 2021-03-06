package com.github.jfwilson.rxjson.core;

import com.github.jfwilson.rxjson.TypeHandler;

import static com.github.jfwilson.rxjson.core.JsonConstantParser.readConstantContent;
import static com.github.jfwilson.rxjson.core.JsonNumberParser.readNumericContent;
import static com.github.jfwilson.rxjson.core.JsonStringParser.readStringContent;

public class TypeReader extends JsonParser {

    private final TypeHandler typeHandler;
    private final JsonParser outerScope;

    public TypeReader(TypeHandler typeHandler, JsonParser outerScope) {
        this.typeHandler = typeHandler;
        this.outerScope = outerScope;
    }

    @Override
    public JsonParser onNext(char c) {
        switch (c) {
            case START_ARRAY:
                return new ArrayReader(typeHandler.onArray(), outerScope);
            case START_OBJECT:
                return new ObjectReader(typeHandler.onObject(), outerScope);
            case DOUBLE_QUOTE:
                return readStringContent(s -> {typeHandler.onString(s); return outerScope;});
            case 't':
                return readConstantContent("true", () -> {typeHandler.onBoolean(true); return outerScope;});
            case 'f':
                return readConstantContent("false", () -> {typeHandler.onBoolean(false); return outerScope;});
            case 'n':
                return readConstantContent("null", () -> {typeHandler.onNull(); return outerScope;});
            default:
                if (c <= '9' && (c == '-' || c >= '0'))
                    return readNumericContent(n -> {typeHandler.onNumber(n); return outerScope;}).onNext(c);
                else
                    return super.onNext(c);
        }
    }
}
