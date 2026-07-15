#include "browse/file_io.hpp"
#include "browse/parser.hpp"

#include <cctype>
#include <filesystem>
#include <fstream>
#include <iomanip>
#include <sstream>
#include <stdexcept>
#include <string>
#include <vector>

namespace browse {
namespace {

std::string read_entire_file(const std::string& path) {
    std::ifstream input(path);
    if (!input) {
        throw std::runtime_error("failed to open input file: " + path);
    }

    std::ostringstream buffer;
    buffer << input.rdbuf();
    return buffer.str();
}

std::string trim(const std::string& value) {
    std::size_t start = 0;
    while (start < value.size() &&
           std::isspace(static_cast<unsigned char>(value[start]))) {
        ++start;
    }

    std::size_t end = value.size();
    while (end > start &&
           std::isspace(static_cast<unsigned char>(value[end - 1]))) {
        --end;
    }
    return value.substr(start, end - start);
}

std::string json_escape(const std::string& value) {
    std::ostringstream out;
    out << '"';
    for (const char c : value) {
        switch (c) {
            case '"': out << "\\\""; break;
            case '\\': out << "\\\\"; break;
            case '\b': out << "\\b"; break;
            case '\f': out << "\\f"; break;
            case '\n': out << "\\n"; break;
            case '\r': out << "\\r"; break;
            case '\t': out << "\\t"; break;
            default:
                if (static_cast<unsigned char>(c) < 0x20) {
                    out << "\\u"
                        << std::hex << std::setfill('0') << std::setw(4)
                        << static_cast<int>(static_cast<unsigned char>(c))
                        << std::dec;
                } else {
                    out << c;
                }
        }
    }
    out << '"';
    return out.str();
}

void ensure_parent_directory(const std::string& path) {
    const std::filesystem::path output_path(path);
    const std::filesystem::path parent = output_path.parent_path();
    if (parent.empty()) {
        return;
    }

    std::error_code error;
    std::filesystem::create_directories(parent, error);
    if (error) {
        throw std::runtime_error(
            "failed to create output directory: " + parent.string());
    }
}

}  // namespace

std::vector<Category> read_categories(const std::string& path) {
    return parse_categories_json(read_entire_file(path));
}

std::vector<Product> read_products(const std::string& path) {
    std::ifstream input(path);
    if (!input) {
        throw std::runtime_error("failed to open input file: " + path);
    }

    std::vector<Product> products;
    std::string line;
    while (std::getline(input, line)) {
        line = trim(line);
        if (!line.empty()) {
            products.push_back(parse_product_jsonl_line(line));
        }
    }
    return products;
}

std::vector<Request> read_requests(const std::string& path) {
    std::ifstream input(path);
    if (!input) {
        throw std::runtime_error("failed to open input file: " + path);
    }

    std::vector<Request> requests;
    std::string line;
    while (std::getline(input, line)) {
        line = trim(line);
        if (!line.empty()) {
            requests.push_back(parse_request_jsonl_line(line));
        }
    }
    return requests;
}

void write_results(const std::string& path,
                   const std::vector<BrowseResult>& results) {
    ensure_parent_directory(path);
    std::ofstream output(path);
    if (!output) {
        throw std::runtime_error("failed to open output file: " + path);
    }

    output << "[\n";
    for (std::size_t i = 0; i < results.size(); ++i) {
        const BrowseResult& result = results[i];
        output << "  {\n"
               << "    \"query_id\": " << json_escape(result.query_id) << ",\n"
               << "    \"matched_count\": " << result.matched_count << ",\n"
               << "    \"products\": [\n";

        for (std::size_t j = 0; j < result.products.size(); ++j) {
            const ResultProduct& product = result.products[j];
            output << "      {\"product_id\": " << json_escape(product.product_id)
                   << ", \"name\": " << json_escape(product.name)
                   << ", \"category_id\": " << json_escape(product.category_id)
                   << ", \"popularity_score\": " << product.popularity_score
                   << "}";
            if (j + 1 < result.products.size()) {
                output << ',';
            }
            output << '\n';
        }

        output << "    ]\n  }";
        if (i + 1 < results.size()) {
            output << ',';
        }
        output << '\n';
    }
    output << "]\n";
}

}  // namespace browse
