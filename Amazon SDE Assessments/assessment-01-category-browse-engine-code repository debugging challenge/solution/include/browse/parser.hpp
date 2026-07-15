#ifndef BROWSE_PARSER_HPP
#define BROWSE_PARSER_HPP

#include "browse/models.hpp"

#include <string>
#include <vector>

namespace browse {

std::vector<Category> parse_categories_json(const std::string& json_doc);
Product parse_product_jsonl_line(const std::string& line);
Request parse_request_jsonl_line(const std::string& line);

}  // namespace browse

#endif  // BROWSE_PARSER_HPP
