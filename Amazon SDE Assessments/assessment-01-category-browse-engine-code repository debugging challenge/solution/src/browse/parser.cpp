#include "browse/parser.hpp"

#include <cctype>
#include <stdexcept>
#include <string>
#include <vector>

namespace browse {
namespace {

std::size_t find_value_start(const std::string& json, const std::string& key) {
    const std::string token = "\"" + key + "\"";
    const std::size_t key_pos = json.find(token);
    if (key_pos == std::string::npos) {
        throw std::runtime_error("missing JSON field: " + key);
    }

    const std::size_t colon = json.find(':', key_pos + token.size());
    if (colon == std::string::npos) {
        throw std::runtime_error("invalid JSON field: " + key);
    }

    std::size_t pos = colon + 1;
    while (pos < json.size() &&
           std::isspace(static_cast<unsigned char>(json[pos]))) {
        ++pos;
    }
    return pos;
}

std::string parse_quoted_string(const std::string& json, std::size_t pos) {
    if (pos >= json.size() || json[pos] != '"') {
        throw std::runtime_error("expected JSON string");
    }

    std::string result;
    for (++pos; pos < json.size(); ++pos) {
        const char c = json[pos];
        if (c == '"') {
            return result;
        }
        if (c == '\\' && pos + 1 < json.size()) {
            const char escaped = json[++pos];
            switch (escaped) {
                case '"': result += '"'; break;
                case '\\': result += '\\'; break;
                case 'n': result += '\n'; break;
                case 'r': result += '\r'; break;
                case 't': result += '\t'; break;
                default: result += escaped; break;
            }
        } else {
            result += c;
        }
    }

    throw std::runtime_error("unterminated JSON string");
}

std::string string_field(const std::string& json, const std::string& key) {
    return parse_quoted_string(json, find_value_start(json, key));
}

int int_field(const std::string& json, const std::string& key) {
    std::size_t pos = find_value_start(json, key);
    bool negative = false;
    if (pos < json.size() && json[pos] == '-') {
        negative = true;
        ++pos;
    }

    if (pos >= json.size() ||
        !std::isdigit(static_cast<unsigned char>(json[pos]))) {
        throw std::runtime_error("expected integer field: " + key);
    }

    int value = 0;
    while (pos < json.size() &&
           std::isdigit(static_cast<unsigned char>(json[pos]))) {
        value = value * 10 + (json[pos] - '0');
        ++pos;
    }
    return negative ? -value : value;
}

bool has_field(const std::string& json, const std::string& key) {
    return json.find("\"" + key + "\"") != std::string::npos;
}

std::optional<std::string> nullable_string_field(
    const std::string& json,
    const std::string& key) {
    const std::size_t pos = find_value_start(json, key);
    if (json.compare(pos, 4, "null") == 0) {
        return std::nullopt;
    }
    return parse_quoted_string(json, pos);
}

std::vector<std::string> extract_objects(const std::string& json) {
    std::vector<std::string> objects;
    bool in_string = false;
    bool escaped = false;
    int depth = 0;
    std::size_t object_start = std::string::npos;

    for (std::size_t i = 0; i < json.size(); ++i) {
        const char c = json[i];
        if (in_string) {
            if (escaped) {
                escaped = false;
            } else if (c == '\\') {
                escaped = true;
            } else if (c == '"') {
                in_string = false;
            }
            continue;
        }

        if (c == '"') {
            in_string = true;
        } else if (c == '{') {
            ++depth;
            if (depth == 2) {
                object_start = i;
            }
        } else if (c == '}') {
            if (depth == 2 && object_start != std::string::npos) {
                objects.push_back(json.substr(object_start, i - object_start + 1));
                object_start = std::string::npos;
            }
            --depth;
        }
    }

    return objects;
}

}  // namespace

std::vector<Category> parse_categories_json(const std::string& json_doc) {
    std::vector<Category> categories;
    for (const std::string& object : extract_objects(json_doc)) {
        Category category;
        category.category_id = string_field(object, "category_id");
        category.name = string_field(object, "name");
        category.parent_id = nullable_string_field(object, "parent_id");
        categories.push_back(std::move(category));
    }
    return categories;
}

Product parse_product_jsonl_line(const std::string& line) {
    Product product;
    product.product_id = string_field(line, "product_id");
    product.name = string_field(line, "name");
    product.category_id = string_field(line, "category_id");
    product.region = string_field(line, "region");
    product.popularity_score = int_field(line, "popularity_score");
    return product;
}

Request parse_request_jsonl_line(const std::string& line) {
    Request request;
    request.query_id = string_field(line, "query_id");
    request.category_id = string_field(line, "category_id");
    request.region = string_field(line, "region");
    request.max_results = has_field(line, "max_results")
        ? int_field(line, "max_results")
        : 5;
    return request;
}

}  // namespace browse
