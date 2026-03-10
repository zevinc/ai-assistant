---
name: pdf-parsing
description: 使用 PyPDF2 从 PDF 文件中提取文本。
compatibility: Python 3.8+, 需要安装 PyPDF2
---

# PDF 解析技能

## 何时使用
当用户需要读取或提取 PDF 文档中的文本内容时使用。

## 工作流程
1.  确认用户提供了 PDF 文件路径。
2.  调用 `scripts/parse_pdf.py` 脚本中的 `extract_text` 函数。
3.  将提取的文本结果返回给用户。

## 脚本使用
本技能提供 `extract_text()` 函数，参数如下：
- `file_path` (str): PDF 文件路径。
- `pages` (str): 指定页数，"all" 或 "1-3"。
