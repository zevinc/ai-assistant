import sys
import json
import PyPDF2

def extract_text(file_path, pages="all"):
    # ... (PDF解析逻辑)
    result = {"success": True, "file_path": file_path, "pages": [...]}
    return json.dumps(result)

if __name__ == "__main__":
    # ... (命令行调用逻辑)
    print(extract_text(sys.argv[2]))
